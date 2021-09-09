package gov.hhs.gsrs.products.productall;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({ProductAllSelector.class, ProductAllConfiguration.class})
public @interface EnableProductAll {
}
