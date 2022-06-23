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
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
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
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    public Optional<String> update(String current_name, String new_name, Long updater) throws CustomSQLException {
        String result;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
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
            try {
                result = callableStatement.getString(1);
            } catch (SQLException e) {
                throw new CustomSQLException(e.getCause().getLocalizedMessage());
            }
            return Optional.of(result);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
