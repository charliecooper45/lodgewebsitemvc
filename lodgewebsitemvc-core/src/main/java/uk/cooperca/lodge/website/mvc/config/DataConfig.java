package uk.cooperca.lodge.website.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import uk.cooperca.lodge.website.mvc.config.profile.ProfileConfig;
import uk.cooperca.lodge.website.mvc.config.profile.impl.DevelopmentProfileConfig;
import uk.cooperca.lodge.website.mvc.config.profile.impl.ProductionProfileConfig;

import java.util.Properties;

/**
 * Main configuration class for data related beans. This class imports another configuration class based on the defined
 * Spring profile.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableJpaRepositories(basePackages = "uk.cooperca.lodge.website.mvc.repository",
        entityManagerFactoryRef = "localContainerEntityManagerFactory")
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Import({DevelopmentProfileConfig.class, ProductionProfileConfig.class})
public class DataConfig {

    @Autowired
    private Environment env;

    @Autowired
    private ProfileConfig profileConfig;

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setShowSql(env.getProperty("vendor.adapter.showSql", Boolean.class));
        return vendorAdapter;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(profileConfig.dataSource());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactory.setPackagesToScan("uk.cooperca.lodge.website.mvc.entity");
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        entityManagerFactory.setJpaProperties(properties);
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactory().getObject());
        return transactionManager;
    }
}
