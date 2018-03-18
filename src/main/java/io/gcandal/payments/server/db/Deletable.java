package io.gcandal.payments.server.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.UUID;

abstract class Deletable<O> extends AbstractDAO<O> {

    Deletable(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public O delete(final UUID id) {
        final O object = get(id);
        currentSession().delete(object);
        return object;
    }
}
