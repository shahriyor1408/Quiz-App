package uz.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uz.hibernate.config.HibernateUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class GenericDAO<T, ID> implements BaseDAO {

    protected SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private Session session = getSession();

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public T save(T entity) {
        Session currentSession = getSession();
        session.beginTransaction();
        currentSession.persist(entity);
        currentSession.getTransaction().commit();
        return entity;
    }

    public void deleteById(ID id) throws SQLException {
        T entity = findById(id);
        // TODO: 6/20/2022 create your custom exception here
        if (Objects.isNull(entity)) {
            throw new SQLException("Object not found by given id '%s'".formatted(id));
        }
        getSession().remove(entity);
    }

    public void update(T entity) {

    }

    public T findById(ID id) {
        return getSession().get(persistentClass, id);
    }

    public List<T> findAll() {
        Session session1 = getSession();
        session1.beginTransaction();
        List<T> resultList = session1.createQuery("from " + persistentClass.getSimpleName(), persistentClass)
                .getResultList();
        session1.getTransaction().commit();
        return resultList;
    }

    protected Session getSession() {
        if (Objects.isNull(sessionFactory) || sessionFactory.isClosed()) {
            sessionFactory = HibernateUtils.getSessionFactory();
        }

        if (Objects.isNull(session) || !session.isOpen()) {
            session = sessionFactory.getCurrentSession();
        }
        return session;
    }

}
