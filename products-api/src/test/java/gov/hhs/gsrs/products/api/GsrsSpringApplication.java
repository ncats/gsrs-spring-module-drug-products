package gov.hhs.gsrs.products.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GsrsSpringApplication {
    static {
        System.out.println("Launching Product Api Test");
    }
    public static void main(String[] args) {
        SpringApplication.run(GsrsSpringApplication.class, args);
    }
}
