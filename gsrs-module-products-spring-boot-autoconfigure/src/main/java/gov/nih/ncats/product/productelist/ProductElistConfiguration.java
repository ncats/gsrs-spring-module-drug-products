package gov.nih.ncats.product.productelist;

import gov.nih.ncats.product.productelist.ProductElistStarterEntityRegistrar;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(ProductElistStarterEntityRegistrar.class)
public class ProductElistConfiguration {
}
