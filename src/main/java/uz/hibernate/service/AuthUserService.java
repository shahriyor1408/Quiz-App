package uz.hibernate.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.domains.SessionEntity;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.Status;
import uz.hibernate.exceptions.CustomSQLException;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.auth.AuthUserCreateVO;
import uz.hibernate.vo.auth.AuthUserUpdateVO;
import uz.hibernate.vo.auth.AuthUserVO;
import uz.hibernate.vo.auth.ResetPasswordVO;
import uz.hibernate.vo.http.Response;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    @Transactional
    public Response<Long> create(@NonNull AuthUserCreateVO vo) {
        // TODO: 6/21/2022 validate input
        Optional<AuthUser> optionalAuthUser = dao.findByUserName(vo.getUsername());
        if (optionalAuthUser.isPresent()) {
            throw new RuntimeException("Username already exist!");
        }

        AuthUser authUser = AuthUser
                .childBuilder()
                .username(vo.getUsername())
                .password(utils.encode(vo.getPassword()))
                .email(vo.getEmail())
                .role(AuthRole.USER)
                .status(Status.ACTIVE)
                .build();
        return new Response<>(dao.save(authUser).getId());
    }

    @Override
    public Response<Void> update(@NonNull AuthUserUpdateVO vo) {
        return null;
    }

    @Override
    public Response<Void> delete(@NonNull Long id) {
        return null;
    }

    @Override
    public Response<AuthUserVO> get(@NonNull Long id) {
        return null;
    }

    @Override
    public Response<List<AuthUserVO>> getAll() {
        return null;
    }

    public static AuthUserService getInstance() {
        if (instance == null) {
            instance = new AuthUserService();
        }
        return instance;
    }

    public Response<AuthUserVO> login(String username, String password) {
        Optional<AuthUser> response = dao.findByUserName(username);

        if (response.isEmpty()) {
            throw new RuntimeException("Username does not exist!");
        }

        AuthUser authUser = response.get();
        if (!utils.matchPassword(password, authUser.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        AuthUserVO authUserVO = AuthUserVO.childBuilder()
                .username(authUser.getUsername())
                .email(authUser.getEmail())
                .role(authUser.getRole())
                .createdAt(authUser.getCreatedAt())
                .build();

        SessionEntity status = SessionEntity.builder()
                .username(authUser.getUsername())
                .firstLoggedIn(Timestamp.valueOf(LocalDateTime.now()))
                .role(authUser.getRole())
                .status(authUser.getStatus())
                .build();

        dao.saveSession(status);
        authUserVO.setId(status.getId());
        Session.setSessionUser(authUserVO);
        return new Response<>(authUserVO);
    }

    public void deleteSession(Long id) {
        Optional<SessionEntity> optionalSession = dao.findByIdSession(id);
        if (optionalSession.isEmpty()) {
            throw new RuntimeException("Session does not exist!");
        }
        try {
            dao.deleteByIdSession(id);
        } catch (CustomSQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetPassword(ResetPasswordVO resetPasswordVO) {
        Optional<SessionEntity> optionalSession = dao.findByIdSession(Session.sessionUser.getId());
        if (optionalSession.isEmpty()) {
            throw new RuntimeException("Session does not exist!");
        }



        if(!resetPasswordVO.getNewPassword().equals(resetPasswordVO.getConfirmPassword())){
            throw new RuntimeException("Confirm password in not valid");
        }

//        if(!utils.matchPassword(resetPasswordVO.getOldPassword(), authUser.getPassword())){
//            throw new RuntimeException("Password is not correct!");
//        }

        resetPasswordVO.setNewPassword(utils.encode(resetPasswordVO.getNewPassword()));
        dao.resetPassword(resetPasswordVO,optionalSession.get().getAuthUser().getId());
    }
}
