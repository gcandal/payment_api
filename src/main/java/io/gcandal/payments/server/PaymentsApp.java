package io.gcandal.payments.server;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.gcandal.payments.server.configuration.PaymentAppsConfiguration;
import io.gcandal.payments.server.db.PaymentDAO;
import io.gcandal.payments.server.db.UserDAO;
import io.gcandal.payments.server.resources.AuthResource;
import io.gcandal.payments.server.resources.PaymentResource;
import io.gcandal.payments.server.resources.PaymentsResource;
import io.gcandal.payments.server.utils.AuthUtils;
import io.gcandal.payments.server.utils.BundleUtils;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;

import java.security.Principal;

public class PaymentsApp extends Application<PaymentAppsConfiguration> {
    private final HibernateBundle<PaymentAppsConfiguration> hibernateBundle = BundleUtils.getHibernateBundle();

    public static void main(String[] args) throws Exception {
        new PaymentsApp().run(args);
    }

    @Override
    public void initialize(final Bootstrap<PaymentAppsConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new EnvironmentVariableSubstitutor(false)
        ));
        bootstrap.addBundle(this.hibernateBundle);
        bootstrap.addBundle(BundleUtils.getMigrationsBundle());
    }

    public void run(final PaymentAppsConfiguration configuration,
                    final Environment environment) throws Exception {
        final SessionFactory sessionFactory = this.hibernateBundle.getSessionFactory();
        final PaymentDAO expenseDAO = new PaymentDAO(sessionFactory);
        final UserDAO userDAO = new UserDAO(sessionFactory);

        environment.jersey().register(AuthUtils.getAuthFeature(
                sessionFactory,
                configuration.getJwtTokenSecret(),
                this.hibernateBundle
        ));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Principal.class));
        environment.jersey().register(new AuthResource(configuration.getJwtTokenSecret(), userDAO));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new PaymentsResource(expenseDAO));
        environment.jersey().register(new PaymentResource(expenseDAO));
    }
}
