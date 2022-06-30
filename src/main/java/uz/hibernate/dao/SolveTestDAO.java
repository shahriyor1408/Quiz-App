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
    public static List<Question> solveTest(String subjectId, QuestionType quizType, String quizNumber) {
        Map<Long, Map<Question, Answer>> questionAnswerMap = new HashMap<>();
        Session currentSession = HibernateUtils.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
        Query<Question> query = currentSession.createQuery("select t from Question t where t.subject.id = :subjectId and t.type = :quizType and t.deleted = false", Question.class);
        query.setParameter("subjectId", subjectId);
        query.setParameter("quizType", quizType);
        List<Question> resultList = query.getResultList();

        currentSession.getTransaction().commit();
        currentSession.close();
        return resultList;
    }

    private static SolveTestDAO instance;

    public static SolveTestDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SolveTestDAO();
        }
        return instance;
    }
}
