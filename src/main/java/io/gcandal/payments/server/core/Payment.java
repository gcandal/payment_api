package io.gcandal.payments.server.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.gcandal.payments.server.db.converters.toJsonConverter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "PAYMENTS")
public class Payment implements Identifiable {

    private static final String LATEST_VERSION = "1";

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private String version;

    @Type(type = "uuid-char")
    @Column(name = "organisation_id")
    private UUID organisationId;

    @Valid
    @Convert(converter = toJsonConverter.class)
    private Attributes attributes;

    public Payment() { }

    @JsonCreator
    public Payment(@JsonProperty("id") final UUID id,
                   @JsonProperty("version") final String version,
                   @JsonProperty("organisation_id") final UUID organisationId,
                   @JsonProperty("attributes") final Attributes attributes) {

        this.id = id;
        this.version = StringUtils.isBlank(version) ? LATEST_VERSION : version;
        this.organisationId = Preconditions.checkNotNull(organisationId);
        this.attributes = Preconditions.checkNotNull(attributes);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void setId(final UUID id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    @JsonProperty("organisation_id")
    public UUID getOrganisationId() {
        return this.organisationId;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.version, this.organisationId, this.attributes);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Payment other = (Payment) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.version, other.version)
                && Objects.equals(this.organisationId, other.organisationId)
                && Objects.equals(this.attributes, other.attributes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("version", this.version)
                .add("organisationId", this.organisationId)
                .add("attributes", this.attributes)
                .toString();
    }
}
