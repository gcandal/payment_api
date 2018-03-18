package io.gcandal.payments.server.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.gcandal.payments.server.core.User;
import io.gcandal.payments.server.utils.AuthUtils;
import io.gcandal.payments.server.utils.PasswordAndSalt;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public User get(final Serializable id) {
        return super.get(id);
    }

    public List<User> getByName(final String name) {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        final Root<User> user = criteria.from(User.class);
        criteria.select(user);
        criteria.where(criteriaBuilder.equal(user.get("name"), name));

        return list(criteria);
    }

    private static void hashPassword(final User user) {
        try {
            final PasswordAndSalt passwordAndSalt = AuthUtils.hash(user.getPassword());
            user.setPassword(passwordAndSalt.getPassword());
            user.setSalt(passwordAndSalt.getSalt());
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
