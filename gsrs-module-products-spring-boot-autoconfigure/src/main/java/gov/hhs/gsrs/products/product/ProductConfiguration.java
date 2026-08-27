package gov.hhs.gsrs.products.product;

import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(DataJpaRepositoriesAutoConfiguration.class)
@Import(ProductStarterEntityRegistrar.class)
public class ProductConfiguration {
}
