package uz.hibernate.dao.quiz;

import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.Answer;
import uz.hibernate.domains.Question;
import uz.jl.BaseUtils;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Objects;
import java.util.Optional;

public class AnswerDAO extends GenericDAO<Question, Long> {

    private static AnswerDAO instance;

    public static AnswerDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new AnswerDAO();
        }
        return instance;
    }


    public Answer saveAnswer(Answer answer) {
        Session currentSession = getSession();
        currentSession.beginTransaction();
        currentSession.persist(answer);
        currentSession.getTransaction().commit();
        return answer;
    }

    public Optional<String> updateAnswer(Answer answer) {

        Session currentSession = HibernateUtils.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
        String answerToJson = BaseUtils.gson.toJson(answer);

        try {
            currentSession.doReturningWork(connection -> {
                CallableStatement function = connection.prepareCall(
                        "{? = call answer_update(?)}");
                function.registerOutParameter(1, Types.VARCHAR);
                function.setString(2, answerToJson);
                function.execute();
                return function;
            });
        } finally {
            currentSession.getTransaction().commit();
            currentSession.close();
        }
        return Optional.empty();
    }

    public Optional<Answer> findAnswerById(Long id){
        Session session = getSession();
        session.beginTransaction();
        Query<Answer> query = session
                .createQuery("select t from Answer t where t.deleted = false and t.id = :id ",
                        Answer.class);
        query.setParameter("id", id);
        Optional<Answer> resultOrNull = Optional.ofNullable(query.getSingleResultOrNull());
        session.getTransaction().commit();
        return resultOrNull;
    }
}