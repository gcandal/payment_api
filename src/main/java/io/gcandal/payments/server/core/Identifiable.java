package io.gcandal.payments.server.core;

import java.util.UUID;

public interface Identifiable {
    UUID getId();

    void setId(final UUID id);
}
