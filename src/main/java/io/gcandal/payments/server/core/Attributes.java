package io.gcandal.payments.server.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Attributes implements Serializable {

    private double amount;

    @JsonCreator
    public Attributes(@JsonProperty("amount") final double amount) {
        Preconditions.checkArgument(amount >= 0, "Amount must be positive.");

        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.amount);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Attributes other = (Attributes) obj;
        return Objects.equals(this.amount, other.amount);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("amount", this.amount)
                .toString();
    }
}
