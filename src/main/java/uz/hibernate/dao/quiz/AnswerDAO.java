package uz.hibernate.dao.quiz;

import org.hibernate.Session;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.Answer;
import uz.hibernate.domains.Question;

import java.util.Objects;

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
}