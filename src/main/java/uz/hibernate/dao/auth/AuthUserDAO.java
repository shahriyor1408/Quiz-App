package uz.hibernate.dao.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.SessionEntity;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.vo.auth.AuthUserUpdateVO;
import uz.hibernate.vo.auth.ResetPasswordVO;
import uz.jl.BaseUtils;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUserDAO extends GenericDAO<AuthUser, Long> {
    private static AuthUserDAO instance;

    public static AuthUserDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new AuthUserDAO();
        }
        return instance;
    }

    public Optional<AuthUser> findByUserName(String username) {
        Session session = getSession();
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        Query<AuthUser> query = session
                .createQuery("select t from AuthUser t where (t.username) = (:username) ",
                        AuthUser.class);
        query.setParameter("username", username);
        Optional<AuthUser> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        return resultOrNull;
    }

    public void saveSession(SessionEntity sessionEntity) {
        Session currentSession = getSession();
        currentSession.beginTransaction();
        currentSession.persist(sessionEntity);
        currentSession.getTransaction().commit();
    }

    public Optional<SessionEntity> findByIdSession(Long id) {
        Session session = getSession();
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        Query<SessionEntity> query = session
                .createQuery("select t from SessionEntity t where (t.id) = (:id) ",
                        SessionEntity.class);
        query.setParameter("id", id);
        Optional<SessionEntity> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        return resultOrNull;
    }

    public void deleteByIdSession(Long id) throws Exception {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        CallableStatement callableStatement = session.doReturningWork(connection -> {
            CallableStatement function = connection.prepareCall(
                    "{ ? = call sessionentity_delete(?)}"
            );
            function.registerOutParameter(1, Types.BIGINT);
            function.setLong(2, id);
            function.execute();
            return function;
        });
        session.getTransaction().commit();
    }

    public void resetPassword(ResetPasswordVO resetPasswordVO, Long id) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{ ? = call reset_password(?,?)}"
                );
                function.registerOutParameter(1, Types.BIGINT);
                function.setString(2, resetPasswordVO.getNewPassword());
                function.setLong(3, id);
                function.execute();
                return function;
            });
        } finally {
            session.getTransaction().commit();
        }
    }

    public void giveTeacherRole(String username, String subjectName) throws Exception {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{ ? = call give_teacher_role(?,?)}"
                );
                function.registerOutParameter(1, Types.BIGINT);
                function.setString(2, username);
                function.setString(3, subjectName);
                function.execute();
                return function;
            });
        } finally {
            session.getTransaction().commit();
        }
    }

    public void updateUser(AuthUserUpdateVO vo) throws Exception {
        Session session = getSession();
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{ ? = call update_user(?)}"
                );
                function.registerOutParameter(1, Types.BIGINT);
                function.setString(2, BaseUtils.gson.toJson(vo));
                function.execute();
                return function;
            });
        } finally {
            session.getTransaction().commit();
        }
    }
}
