/**
 * SaasCorePersistenceConfigurer.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.config.persistence;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * SaasCorePersistenceConfigurer.
 * <<< Authorization data source connection.
 * @author hieumicro
 * @access public
 */
@Configuration
@EnableJpaRepositories(basePackages = "co.ipicorp.saas.core.dao",
        entityManagerFactoryRef = "authEntityManager", transactionManagerRef = "authTransactionManager")
public class SaasCorePersistenceConfigurer {
    @Autowired
    private Environment env;
    
    /**
     * Initialization LocalContainerEntityManagerFactoryBean for Authorization database.
     * @return {@link LocalContainerEntityManagerFactoryBean} instance
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean authEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(authDataSource());
        em.setPackagesToScan(new String[] { "co.ipicorp.saas.core.model", "grass.micro.apps.model.base" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.order_inserts", "true");
        properties.put("hibernate.order_updates", "true");
        properties.put("hibernate.jdbc.batch_size", "2");
//        properties.put("hibernate.enable_lazy_load_no_trans", "true");
        properties.put("hibernate.show_sql", env.getProperty("show_sql"));
        
        em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * Initialization DataSource for Authorization database.
     * @return {@link DataSource} instance
     */
    @Primary
    @Bean
    public DataSource authDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));

        return dataSource;
    }

    /**
     * Initialization PlatformTransactionManager for Authorization database.
     * @return {@link PlatformTransactionManager} instance
     */
    @Primary
    @Bean
    public PlatformTransactionManager authTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(authEntityManager().getObject());
        return transactionManager;
    }
    
    /**
     * PersistenceExceptionTranslationPostProcessor is a bean post processor which adds an advisor to any bean annotated
     * with Repository so that any platform-specific exceptions are caught and then re-thrown as one Spring's unchecked
     * data access exceptions (i.e. a subclass of DataAccessException).
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
