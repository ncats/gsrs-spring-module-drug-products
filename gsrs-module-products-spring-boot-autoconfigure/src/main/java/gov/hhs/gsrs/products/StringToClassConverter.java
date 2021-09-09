package gov.hhs.gsrs.products;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class StringToClassConverter implements Converter<String, Class> {
    @Override
    public Class convert(String s) {
        try {
            return StringToClassConverter.class.getClassLoader().loadClass(s);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("could not load class '"+ s+ "'", e);
        }
    }
}
