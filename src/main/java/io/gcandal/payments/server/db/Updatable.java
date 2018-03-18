package io.gcandal.payments.server.db;

import java.io.Serializable;

public interface Updatable<O> {
    O update(O object);
    O get(Serializable id);
}
