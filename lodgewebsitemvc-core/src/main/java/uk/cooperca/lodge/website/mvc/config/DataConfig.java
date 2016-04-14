package uk.cooperca.lodge.website.mvc.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.cooperca.lodge.website.mvc.config.profile.ProfileConfig;
import uk.cooperca.lodge.website.mvc.config.profile.impl.DevelopmentProfileConfig;
import uk.cooperca.lodge.website.mvc.config.profile.impl.ProductionProfileConfig;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * The main configuration class for data related beans. Imports another configuration class based on the defined profile.
 *
 * @author Charlie Cooper
 */
@Configuration
@EnableJpaRepositories(basePackages = "uk.cooperca.lodge.website.mvc.repository",
        entityManagerFactoryRef = "localContainerEntityManagerFactory")
@ComponentScan(basePackages = { "uk.cooperca.lodge.website.mvc.service" })
@EnableTransactionManagement
@EnableEncryptableProperties
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
    public EntityManagerFactory localContainerEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(profileConfig.dataSource());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactory.setPackagesToScan("uk.cooperca.lodge.website.mvc.entity");
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        entityManagerFactory.setJpaProperties(properties);
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactory());
        return transactionManager;
    }
}
