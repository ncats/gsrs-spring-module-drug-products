package gov.nih.ncats.product.productall.indexers;

import gov.nih.ncats.product.productall.models.*;
import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProductIndexValueMaker implements IndexValueMaker<ProductMainAll> {
    private static final Pattern SPLIT = Pattern.compile("(\\s+)");
    @Override
    public Class<ProductMainAll> getIndexedEntityClass() {
        return ProductMainAll.class;
    }

    @Override
    public void createIndexableValues(ProductMainAll product, Consumer<IndexableValue> consumer) {
        String id = product.productId;
        //if(title ==null){
        //    return;
       // }

      //  consumer.accept(IndexableValue.simpleFacetLongValue("application.id", id));
        /*
        Matcher matcher = SPLIT.matcher(title);
        if(matcher.find()){
            String firstName = title.substring(0, matcher.start(1));
            String lastName = title.substring(matcher.end(1));
            consumer.accept(IndexableValue.simpleFacetStringValue("author.firstName", firstName));
            consumer.accept(IndexableValue.simpleFacetStringValue("author.lastName", lastName));
        }
         */
    }
}
