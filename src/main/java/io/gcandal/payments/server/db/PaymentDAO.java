package io.gcandal.payments.server.db;

import io.gcandal.payments.server.core.Payment;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class PaymentDAO extends Deletable<Payment> implements Updatable<Payment> {

    public PaymentDAO(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Payment create(final Payment payment) {
        return persist(payment);
    }

    @Override
    public Payment get(final Serializable id) {
        return super.get(id);
    }

    public List<Payment> list() {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Payment> criteria = criteriaBuilder.createQuery(getEntityClass());
        final Root<Payment> root = criteria.from(getEntityClass());
        criteria.select(root);

        return list(criteria);
    }

    public Payment update(final Payment payment) {
        return persist(payment);
    }
}
