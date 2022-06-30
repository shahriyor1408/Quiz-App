package uz.hibernate.dao.subject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.exceptions.CustomSQLException;
import uz.hibernate.service.GenericCRUDService;
import uz.hibernate.vo.BaseVO;
import uz.hibernate.vo.GenericVO;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubjectDAO extends GenericDAO<Subject, Long> {

    private static SubjectDAO instance;

    public static SubjectDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SubjectDAO();
        }
        return instance;
    }

    public Optional<Subject> findByName(String in_name) {
        Session session = getSession();
        session.beginTransaction();
        Query<Subject> query = session
                .createQuery("select t from Subject t where lower(t.name) = lower(:in_name) ",
                        Subject.class);
        query.setParameter("in_name", in_name);
        Optional<Subject> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        session.close();
        return resultOrNull;
    }

    public void update(String current_name, String new_name, Long updater) throws SQLException {
        Session session = getSession();
        session.beginTransaction();
        CallableStatement callableStatement = session.doReturningWork(connection -> {
            CallableStatement function = connection.prepareCall(
                    "{? = call subject_update(?,?,?)}"
            );
            function.registerOutParameter(1, Types.VARCHAR);
            function.setString(2, current_name);
            function.setString(3, new_name);
            function.setLong(4, updater);
            function.execute();
            return function;
        });
        callableStatement.getString(1);
        session.getTransaction().commit();
        session.close();
    }

    public Optional<List<Subject>> subjectShowList() {
        Session currentSession = getSession();
        currentSession.beginTransaction();
        List<Subject> subjects = currentSession.createQuery("select t from Subject t where t.deleted = false", Subject.class).getResultList();
        Optional<List<Subject>> subjectList = Optional.of(subjects);
        currentSession.getTransaction().commit();
        currentSession.close();
        return subjectList;
    }

    public Optional<Subject> findByUserId(Long userId) {
        Session session = getSession();
        session.beginTransaction();
        Query<Subject> query = session
                .createQuery("select t from Subject t where t.deleted = false and (t.authUser.id) = (:userId) ",
                        Subject.class);
        query.setParameter("userId", userId);
        Optional<Subject> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        session.close();
        return resultOrNull;
    }

    public Optional<Subject> findSubjectById(Long id) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        if (!session.getTransaction().isActive()) {
            session.beginTransaction();
        }
        Query<Subject> query = session
                .createQuery("select t from Subject t where t.id = :id ",
                        Subject.class);
        query.setParameter("id", id);
        Optional<Subject> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        session.close();
        return resultOrNull;
    }

    public Optional<String> delete(String name, Long updater) throws SQLException {
        String result;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{ ? = call subject_delete(?,?) }"
                );
                function.registerOutParameter(1, Types.VARCHAR);
                function.setString(2, name);
                function.setLong(3, Math.toIntExact(updater));
                function.execute();
                return function;
            });
            result = callableStatement.getString(1);
            return Optional.of(result);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
