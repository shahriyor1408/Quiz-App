package uz.hibernate.dao.quiz;

import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.Question;

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

    public Optional<Question> findByText(String in_text) {
        Session session = getSession();
        session.beginTransaction();
        Query<Question> query = session
                .createQuery("select t from Question t where lower(t.text) = lower(:in_text) ",
                        Question.class);
        query.setParameter("in_text", in_text);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }
}