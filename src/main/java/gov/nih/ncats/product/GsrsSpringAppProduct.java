package gov.nih.ncats.product;

import gsrs.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.context.properties.*;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/*
@SpringBootApplication
@EnableGsrsApi(indexerType = EnableGsrsApi.IndexerType.LEGACY,
        entityProcessorDetector = EnableGsrsApi.EntityProcessorDetector.CONF)
@EnableGsrsJpaEntities
// @EnableGsrsLegacyAuthentication
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableJpaRepositories(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableAutoConfiguration (exclude = {  DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = "gov.nih.ncats.application")
*/

/*
June 4, 2021
@SpringBootApplication
@EnableGsrsApi
@EnableGsrsJpaEntities
@EnableGsrsLegacyAuthentication
@EnableGsrsLegacyCache
@EnableGsrsLegacyPayload
@EnableGsrsLegacySequenceSearch
@EnableGsrsLegacyStructureSearch
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableJpaRepositories(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableGsrsScheduler
@EnableGsrsBackup
public class GsrsModuleSubstanceApplication {
 */
@SpringBootApplication
@EnableGsrsApi
@EnableGsrsJpaEntities
//@EnableGsrsLegacyAuthentication
@EnableGsrsLegacyCache
@EnableGsrsLegacyPayload
@EnableGsrsLegacySequenceSearch
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableJpaRepositories(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
//@EnableGsrsScheduler
//EnableGsrsBackup

public class GsrsSpringAppProduct {

    public static void main(String[] args) {
        SpringApplication.run(GsrsSpringAppProduct.class, args);
    }
}
