package uz.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import uz.hibernate.config.HibernateUtils;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.http.Response;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        currentSession.beginTransaction();
        currentSession.persist(entity);
        currentSession.getTransaction().commit();
        currentSession.close();
        return entity;
    }

    public void deleteById(ID id) throws SQLException {
        // TODO: 6/20/2022 create your custom exception here
        Session session = getSession();
        session.beginTransaction();
        CallableStatement callableStatement = session.doReturningWork(connection -> {
            CallableStatement function = connection.prepareCall(
                    "{ ? = call user_delete(?)}"
            );
            function.registerOutParameter(1, Types.BIGINT);
            function.setLong(2, (Long) id);
            function.execute();
            return function;
        });
        session.getTransaction().commit();
        session.close();
    }

    public void update(T entity) {

    }

    public T findById(ID id) {
        Session currentSession = getSession();
        if (!currentSession.getTransaction().isActive()) {
            currentSession.beginTransaction();
        }
        T t = currentSession.get(persistentClass, id);
        currentSession.getTransaction().commit();
        currentSession.close();
        return t;
    }

    public List<T> findAll() {
        Session session1 = getSession();
        if (!session1.getTransaction().isActive()) {
            session1.beginTransaction();
        }
        List<T> resultList = session1.createQuery("from " + persistentClass.getSimpleName(), persistentClass)
                .getResultList();
        session1.getTransaction().commit();
        session.close();
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
