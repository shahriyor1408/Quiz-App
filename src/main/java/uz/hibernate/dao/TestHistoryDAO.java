package uz.hibernate.dao;

import org.hibernate.Session;
import uz.hibernate.domains.TestHistory;

import java.util.Objects;

public class TestHistoryDAO extends GenericDAO<TestHistory, Long> {
    private static TestHistoryDAO instance;


    public static TestHistoryDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new TestHistoryDAO();
        }
        return instance;
    }

    public void saveTestHistory(TestHistory testHistory) {
        Session currentSession = getSession();
        currentSession.beginTransaction();
        currentSession.persist(testHistory);
        currentSession.getTransaction().commit();
        currentSession.close();
    }

}
