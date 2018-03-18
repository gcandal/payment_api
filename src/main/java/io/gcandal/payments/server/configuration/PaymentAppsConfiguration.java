package io.gcandal.payments.server.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

public class PaymentAppsConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private final DataSourceFactory database;

    @NotEmpty
    @JsonProperty
    @Length(min = 32)
    private final String jwtTokenSecret;

    @JsonCreator
    public PaymentAppsConfiguration(@JsonProperty("database") final DataSourceFactory database,
                                    @JsonProperty("jwtTokenSecret") final String jwtTokenSecret) {

        this.database = Preconditions.checkNotNull(database);
        this.jwtTokenSecret = Preconditions.checkNotNull(jwtTokenSecret);
    }

    public DataSourceFactory getDataSourceFactory() {
        return this.database;
    }

    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return this.jwtTokenSecret.getBytes("UTF-8");
    }
}
