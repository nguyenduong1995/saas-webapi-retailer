/**
 * SaasNrmsPersistenceConfigurer.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.retailerapi.config.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

import javax.sql.DataSource;

/**
 * SaasNrmsPersistenceConfigurer.
 * <<< Nrms data source connection.
 * @author hieumicro
 * @access public
 */
@Configuration
@EnableJpaRepositories(basePackages = "co.ipicorp.saas.nrms.dao",
        entityManagerFactoryRef = "nrmsEntityManager", transactionManagerRef = "nrmsTransactionManager")
public class SaasNrmsPersistenceConfigurer {
    @Autowired
    private Environment env;
    
    /**
     * Initialization LocalContainerEntityManagerFactoryBean for NRMS database.
     * @return {@link LocalContainerEntityManagerFactoryBean} instance
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean nrmsEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(nrmsDataSource());
        em.setPackagesToScan(new String[] { "co.ipicorp.saas.nrms.model", "grass.micro.apps.model.base" });

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
     * Initialization DataSource for NRMS database.
     * @return {@link DataSource} instance
     */
    @Bean
    public DataSource nrmsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("nrms.jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("nrms.jdbc.url"));
        dataSource.setUsername(env.getProperty("nrms.jdbc.user"));
        dataSource.setPassword(env.getProperty("nrms.jdbc.pass"));

        return dataSource;
    }

    /**
     * Initialization PlatformTransactionManager for NRMS database.
     * @return {@link PlatformTransactionManager} instance
     */
    @Bean
    public PlatformTransactionManager nrmsTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(nrmsEntityManager().getObject());
        return transactionManager;
    }
    
}
