package gov.nih.ncats.product;

import gsrs.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.*;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

    @Configuration
    @PropertySource("classpath:application.properties")
    public class DataSourceConfiguration {

        @Autowired
        Environment environment;

        @Bean
        public DataSource datasource() {

            System.out.println("****************** " + environment.getProperty("spring.datasource.username"));
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
            dataSource.setUrl("jdbc:oracle:thin:@//D15311532.fda.gov:1532/SRSIDDEV");
          //  dataSource.setUsername("SRSCID");
          //  dataSource.setPassword("App4gsrs!");
            dataSource.setUsername("GSRS_PROD");
            dataSource.setPassword("prd_JAN_2016");
            return dataSource;

            /*
            final DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
            dataSource.setUrl(environment.getProperty("spring.datasource.url"));
            dataSource.setUsername(environment.getProperty("spring.datasource.username"));
            dataSource.setPassword(environment.getProperty("spring.datasource.password"));
            return dataSource;
            */

        }
    }
