package uz.hibernate.dao.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.SessionEntity;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.exceptions.CustomSQLException;
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
        session.beginTransaction();
        Query<AuthUser> query = session
                .createQuery("select t from AuthUser t where lower(t.username) = lower(:username) ",
                        AuthUser.class);
        query.setParameter("username", username);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    public void saveSession(SessionEntity sessionEntity) {
        Session currentSession = getSession();
        currentSession.persist(sessionEntity);
        currentSession.getTransaction().commit();
    }

    public Optional<SessionEntity> findByIdSession(Long id) {
        Session session = getSession();
        session.beginTransaction();
        Query<SessionEntity> query = session
                .createQuery("select t from SessionEntity t where (t.id) = (:id) ",
                        SessionEntity.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    public void deleteByIdSession(Long id) throws CustomSQLException {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{ ? = call sessionentity_delete(?)}"
                );
                function.registerOutParameter(1, Types.BIGINT);
                function.setLong(2, id);
                function.execute();
                return function;
            });
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void resetPassword(ResetPasswordVO resetPasswordVO, Long id) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{ ? = call reset_password(?,?)}"
                );
                function.registerOutParameter(1, Types.BIGINT);
                function.setString(2, BaseUtils.gson.toJson(resetPasswordVO));
                function.setLong(3, id);
                function.execute();
                return function;
            });
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public Optional subjectShowList() {
        Session currentSession = getSession();
        currentSession.beginTransaction();
        List subject = currentSession.createQuery("FROM Subject").list();
        currentSession.getTransaction().commit();
        return Optional.ofNullable(subject);
    }
}
