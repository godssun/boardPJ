package com.github.boardpj.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
		basePackages = {"com.github.boardpj.repository.comment", "com.github.boardpj.repository.post", "com.github.boardpj.repository.user"},
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager"
)
public class JpaConfig {

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.github.boardpj.repository.comment", "com.github.boardpj.repository.post", "com.github.boardpj.repository.user");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);


		return em;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return transactionManager;
	}
}