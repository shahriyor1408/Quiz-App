package uz.hibernate.dao.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import uz.hibernate.dao.GenericDAO;
import uz.hibernate.domains.SessionEntity;
import uz.hibernate.domains.auth.AuthUser;

import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUserDAO extends GenericDAO<AuthUser, Long> {
    private static AuthUserDAO instance;

    public static AuthUserDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new AuthUserDAO();
        }
        return instance;
    }

    public Optional<AuthUser> findByUserName(String username) {
        Session session = getSession();
        session.beginTransaction();
        Query<AuthUser> query = session
                .createQuery("select t from AuthUser t where lower(t.username) = lower(:username) ",
                        AuthUser.class);
        query.setParameter("username", username);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    public void saveSession(SessionEntity.SessionEntityBuilder sessionEntity) {

    }
}
