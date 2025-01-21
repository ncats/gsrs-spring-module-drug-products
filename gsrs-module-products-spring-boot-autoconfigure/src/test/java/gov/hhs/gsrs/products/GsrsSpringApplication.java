package gov.hhs.gsrs.products;

import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import ix.core.search.bulk.EnableBulkSearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableConfigurationProperties
@EnableGsrsApi(indexerType = EnableGsrsApi.IndexerType.LEGACY,
        entityProcessorDetector = EnableGsrsApi.EntityProcessorDetector.CUSTOM,
        indexValueMakerDetector = EnableGsrsApi.IndexValueMakerDetector.CUSTOM)
@EnableGsrsJpaEntities
@SpringBootApplication
@EnableBulkSearch
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
@EnableJpaRepositories(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
public class GsrsSpringApplication {

    static {
        System.out.println("Launching Product Test");
    }

    public static void main(String[] args) {
        SpringApplication.run(GsrsSpringApplication.class, args);
    }
}