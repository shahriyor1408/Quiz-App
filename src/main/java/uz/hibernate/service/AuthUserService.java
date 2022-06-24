package uz.hibernate.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.dao.subject.SubjectDAO;
import uz.hibernate.domains.SessionEntity;
import uz.hibernate.domains.Subject;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.Status;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.auth.AuthUserCreateVO;
import uz.hibernate.vo.auth.AuthUserUpdateVO;
import uz.hibernate.vo.auth.AuthUserVO;
import uz.hibernate.vo.auth.ResetPasswordVO;
import uz.hibernate.vo.http.Response;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthUserService extends AbstractDAO<AuthUserDAO> implements GenericCRUDService<
        AuthUserVO,
        AuthUserCreateVO,
        AuthUserUpdateVO,
        Long> {

    private static AuthUserService instance;
//    private final AuthUserValidator validator;

    private AuthUserService() {
        super(
                ApplicationContextHolder.getBean(AuthUserDAO.class),
                ApplicationContextHolder.getBean(BaseUtil.class)
        );
    }

    static SubjectService subjectService = SubjectService.getInstance();

    @Override
    @Transactional
    public Response<DataVO<Long>> create(@NonNull AuthUserCreateVO vo) {
        // TODO: 6/21/2022 validate input
        Optional<AuthUser> optionalAuthUser = dao.findByUserName(vo.getUsername());
        if (optionalAuthUser.isPresent()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Username already exist!")
                    .build()), false);
        }

        AuthUser authUser = AuthUser
                .childBuilder()
                .username(vo.getUsername())
                .password(utils.encode(vo.getPassword()))
                .email(vo.getEmail())
                .role(AuthRole.USER)
                .status(Status.ACTIVE)
                .createdBy(-1L)
                .build();
        return new Response<>(new DataVO<>(dao.save(authUser).getId()));
    }

    @Override
    public Response<DataVO<Void>> update(@NonNull AuthUserUpdateVO vo) {
        try {
            dao.updateUser(vo);
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .build()), false);
        }
    }

    @Override
    public Response<DataVO<Void>> delete(@NonNull Long id) {
        try {
            dao.deleteById(id);
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .build()), false);
        }
    }

    @Override
    public Response<DataVO<AuthUserVO>> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<List<AuthUserVO>>> getAll() {
        List<AuthUser> authUsers = dao.findAll();
        List<AuthUserVO> userVOList = new ArrayList<>();

        for (AuthUser authUser : authUsers) {
            userVOList.add(AuthUserVO.childBuilder()
                    .id(authUser.getId())
                    .username(authUser.getUsername())
                    .email(authUser.getEmail())
                    .role(authUser.getRole())
                    .createdAt(authUser.getCreatedAt())
                    .is_deleted(authUser.isDeleted())
                    .build());
        }

        return new Response<>(new DataVO<>(userVOList));
    }

    public static AuthUserService getInstance() {
        if (instance == null) {
            instance = new AuthUserService();
        }
        return instance;
    }

    public Response<DataVO<AuthUserVO>> login(String username, String password) {
        Optional<AuthUser> response = dao.findByUserName(username);

        if (response.isEmpty() || response.get().isDeleted()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("User not found!")
                    .build()), false);
        }

        AuthUser authUser = response.get();
        if (!utils.matchPassword(password, authUser.getPassword())) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Bad credentials!")
                    .build()), false);
        }

        AuthUserVO authUserVO = AuthUserVO.childBuilder()
                .id(authUser.getId())
                .username(authUser.getUsername())
                .email(authUser.getEmail())
                .role(authUser.getRole())
                .createdAt(authUser.getCreatedAt())
                .userId(authUser.getId())
                .build();

        SessionEntity status = SessionEntity.builder()
                .username(authUser.getUsername())
                .firstLoggedIn(Timestamp.valueOf(LocalDateTime.now()))
                .role(authUser.getRole())
                .status(authUser.getStatus())
                .authUser(authUser)
                .build();
        dao.saveSession(status);
        authUserVO.setId(status.getId());
        Session.setSessionUser(authUserVO);
        return new Response<>(new DataVO<>(authUserVO));
    }

    public Response<DataVO<Void>> deleteSession(Long id) {
        Optional<SessionEntity> optionalSession = dao.findByIdSession(id);
        if (optionalSession.isEmpty()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Session user not found!")
                    .build()), false);
        }
        try {
            dao.deleteByIdSession(id);
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .developerMessage(e.getCause().getLocalizedMessage())
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .build()));
        }
    }

    public Response<DataVO<Void>> resetPassword(ResetPasswordVO resetPasswordVO) {
        AuthUser authUser = dao.findById(Session.sessionUser.getUserId());

        if (Objects.isNull(authUser)) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("User not found!")
                    .build()), false);
        }

        if (!resetPasswordVO.getNewPassword().equals(resetPasswordVO.getConfirmPassword())) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Confirm password is not valid!")
                    .build()), false);
        }

        if (utils.matchPassword(resetPasswordVO.getOldPassword(), authUser.getPassword())) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Old password is not valid!")
                    .build()), false);
        }
        resetPasswordVO.setNewPassword(utils.encode(resetPasswordVO.getNewPassword()));
        dao.resetPassword(resetPasswordVO, Session.sessionUser.getUserId());
        return new Response<>(new DataVO<>(null, true));
    }

    public Response<DataVO<Void>> giveTeacherPermission(String username, String subjectName) {
        Optional<AuthUser> userOptional = dao.findByUserName(username);

        if (userOptional.isEmpty() || userOptional.get().isDeleted()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Such user not found!")
                    .build()), false);
        }
        Optional<Subject> subjectOptional = subjectService.findByName(subjectName);
        if (subjectOptional.isEmpty() || subjectOptional.get().isDeleted()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Subject not found!")
                    .build()), false);
        }
        try {
            dao.giveTeacherRole(username, subjectName);
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .developerMessage(e.getCause().getLocalizedMessage())
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .build()), false);
        }
    }
}
