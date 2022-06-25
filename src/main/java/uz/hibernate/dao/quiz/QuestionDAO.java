package uz.hibernate.dao.quiz;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.sql.exec.ExecutionException;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.Question;
import uz.hibernate.enums.QuestionType;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Optional;

public class QuestionDAO extends GenericDAO<Question, Long> {

    private static QuestionDAO instance;

    public static QuestionDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new QuestionDAO();
        }
        return instance;
    }

    public Optional<Question> findByText(String in_text) throws Exception {
        Session session = getSession();
        session.beginTransaction();
        Query<Question> query = session
                .createQuery("select t from Question t where (t.text) = (:in_text) ",
                        Question.class);
        query.setParameter("in_text", in_text);
        Optional<Question> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        session.close();
        return resultOrNull;
    }

    public void update(String textForFind, String text, QuestionType type) throws Exception {
        Session currentSession = getSession();
        currentSession.beginTransaction();
        try {
            CallableStatement callableStatement = currentSession.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{? = call question_update(?,?,?)}");
                function.registerOutParameter(1, Types.BIGINT);
                function.setString(2, textForFind);
                function.setString(3, text);
                function.setString(4, String.valueOf(type));
                function.execute();
                return function;
            });
        } finally {
            currentSession.getTransaction().commit();
            currentSession.close();
        }
    }

    public Optional<Question> findByQuestionId(Long id) {
        Session session = getSession();
        session.beginTransaction();
        Query<Question> query = session
                .createQuery("select t from Question t where t.id = :questionId and t.deleted = false",
                        Question.class);
        query.setParameter("questionId", id);
        Optional<Question> singleResultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        session.close();
        return singleResultOrNull;
    }

    public void delete(Long id) throws SQLException, ExecutionException {
        Session session = getSession();
        session.beginTransaction();
        try {
            CallableStatement callableStatement = session.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{? = call question_delete(?)}");
                function.registerOutParameter(1, Types.VARCHAR);
                function.setString(2, String.valueOf(id));
                function.execute();
                return function;
            });
            callableStatement.getString(1);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}