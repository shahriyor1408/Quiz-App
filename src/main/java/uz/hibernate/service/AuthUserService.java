package uz.hibernate.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.auth.AuthUserCreateVO;
import uz.hibernate.vo.auth.AuthUserUpdateVO;
import uz.hibernate.vo.auth.AuthUserVO;
import uz.hibernate.vo.http.Response;

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
                .createdAt(authUser.getCreatedAt())
                .build();
        Session.setSessionUser(authUserVO);
        return new Response<>(authUserVO);
    }
}
