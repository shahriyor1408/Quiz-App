package uz.hibernate.dao.subject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.exceptions.CustomSQLException;

import java.sql.CallableStatement;
import java.sql.SQLException;
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
        if(!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        Query<Subject> query = session
                .createQuery("select t from Subject t where lower(t.name) = lower(:in_name) ",
                        Subject.class);
        query.setParameter("in_name", in_name);
        Optional<Subject> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        return resultOrNull;
    }

    public Optional<String> update(String current_name, String new_name, Long updater) throws SQLException {
        String result;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        CallableStatement callableStatement = session.doReturningWork(connection -> {
            CallableStatement function = connection.prepareCall(
                    "select subject_update(?,?,?)"
            );
            function.setString(1, current_name);
            function.setString(2, new_name);
            function.setString(3, String.valueOf(updater));
            function.execute();
            return function;
        });
        result = callableStatement.getString(1);
        Optional<String> optional = Optional.of(result);
        session.getTransaction().commit();
        return optional;
    }

    public Optional<List<Subject>> subjectShowList() {
        Session currentSession = getSession();
        if(!currentSession.getTransaction().isActive()){
            currentSession.beginTransaction();
        }
        List<Subject> subjects = currentSession.createQuery("from Subject", Subject.class).getResultList();
        Optional<List<Subject>> subjectList = Optional.of(subjects);
        currentSession.getTransaction().commit();
        return subjectList;
    }

    public Optional<Subject> findByUserId(Long userId) {
        Session session = getSession();
        if(!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        Query<Subject> query = session
                .createQuery("select t from Subject t where (t.authUser.id) = (:userId) ",
                        Subject.class);
        query.setParameter("userId", userId);
        Optional<Subject> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        return resultOrNull;
    }
}
