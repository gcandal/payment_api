package io.gcandal.payments.server.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.security.Principal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
@Table(
        name = "USERS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
public class User implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 5, max = 20)
    @NotBlank
    private String name;

    @Length(min = 5, max = 20)
    private String password;

    private String salt;

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
