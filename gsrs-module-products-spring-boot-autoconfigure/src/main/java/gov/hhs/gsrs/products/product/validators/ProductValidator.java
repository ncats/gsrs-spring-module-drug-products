package gov.hhs.gsrs.products.product.validators;

import gov.hhs.gsrs.products.product.models.*;

import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

public class ProductValidator implements ValidatorPlugin<Product> {

    @Override
    public boolean supports(Product newValue, Product oldValue, ValidatorConfig.METHOD_TYPE methodType) {
        return true;
    }

    @Override
    public void validate(Product objnew, Product objold, ValidatorCallback callback) {
    }
}
