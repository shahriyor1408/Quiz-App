package uz.hibernate.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.domains.Answer;
import uz.hibernate.domains.Question;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.enums.QuestionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolveTestDAO extends GenericDAO<AuthUser, Long> {
    public static Map<Long, Map<Question, Answer>> solveTest(String subjectId, QuestionType quizType, String quizNumber) {
        Map<Long, Map<Question, Answer>> questionAnswerMap = new HashMap<>();
        Session currentSession = HibernateUtils.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
        Query<Question> query = currentSession.createQuery("select t from Question t where t.subject.id = :subjectId and t.type = :quizType and t.deleted = false", Question.class);
        query.setParameter("subjectId", subjectId);
        query.setParameter("quizType", quizType);
        query.setMaxResults(Integer.parseInt(quizNumber));
        List<Question> resultList = query.getResultList();

        long i = 1L;
        Map<Question, Answer> questionAnswerMap1 = new HashMap<>();
        for (Question question : resultList) {
            Long id = question.getId();
            Query<Answer> query1 = currentSession.createQuery("select t from Answer t where t.question.id = :id and t.deleted = false", Answer.class);
            query1.setParameter("id", id);
            Answer answer = query1.getSingleResult();
            questionAnswerMap1.put(question, answer);
            questionAnswerMap.put(i, questionAnswerMap1);
            i++;
        }

        currentSession.getTransaction().commit();
        currentSession.close();
        return questionAnswerMap;
    }

    private static SolveTestDAO instance;

    public static SolveTestDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SolveTestDAO();
        }
        return instance;
    }
}
