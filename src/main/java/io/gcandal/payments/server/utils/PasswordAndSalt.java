package io.gcandal.payments.server.utils;

public class PasswordAndSalt {
    private final String password;
    private final String salt;

    public PasswordAndSalt(final String password, final String salt) {
        this.password = password;
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }
}
