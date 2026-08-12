package gov.hhs.gsrs.products.autoconfigure;

import gsrs.api.AbstractGsrsRestApiConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@ConfigurationProperties("gsrs.microservice.substances.api")
public class SubstancesApiConfiguration extends AbstractGsrsRestApiConfiguration {

}

