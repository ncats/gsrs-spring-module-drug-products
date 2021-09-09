package gov.hhs.gsrs.products.product;

import gov.hhs.gsrs.products.product.ProductStarterEntityRegistrar;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(ProductStarterEntityRegistrar.class)
public class ProductConfiguration {
}
