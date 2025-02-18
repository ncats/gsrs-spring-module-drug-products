package gov.hhs.gsrs.products.api;

import ix.seqaln.service.LegacySequenceIndexerService;
import ix.seqaln.service.SequenceIndexerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class GsrsSpringApplication {
    static {
        System.out.println("Launching Product Api Test");
    }
    public static void main(String[] args) {
        SpringApplication.run(GsrsSpringApplication.class, args);
    }

}
