package io.gcandal.payments.server.db;

public interface Updatable<O> {
    O update(final O object);
}
