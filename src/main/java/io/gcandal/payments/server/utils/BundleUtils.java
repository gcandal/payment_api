package io.gcandal.payments.server.utils;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.gcandal.payments.server.configuration.PaymentAppsConfiguration;
import io.gcandal.payments.server.core.Payment;
import io.gcandal.payments.server.core.User;
import org.hibernate.cfg.Configuration;

public final class BundleUtils {
    private BundleUtils() { }

    public static HibernateBundle<PaymentAppsConfiguration> getHibernateBundle() {
        return new HibernateBundle<PaymentAppsConfiguration>(Payment.class, User.class) {
            @Override
            public DataSourceFactory getDataSourceFactory(final PaymentAppsConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            protected void configure(final Configuration configuration) {
                configuration.setProperty("javax.persistence.validation.factory", "");
                super.configure(configuration);
            }
        };
    }

    public static MigrationsBundle<PaymentAppsConfiguration> getMigrationsBundle() {
        return new MigrationsBundle<PaymentAppsConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(final PaymentAppsConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }
}
