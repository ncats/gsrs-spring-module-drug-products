package gov.nih.ncats.product.productall;

import gov.nih.ncats.product.productall.ProductAllStarterEntityRegistrar;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(ProductAllStarterEntityRegistrar.class)
public class ProductAllConfiguration {
}
