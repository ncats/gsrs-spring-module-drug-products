package gov.nih.ncats.product.validators;

import gov.nih.ncats.product.model.Product;
import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

public class RequiredFieldNonNullValidator implements ValidatorPlugin<Product> {

    @Override
    public boolean supports(Product newValue, Product oldValue, ValidatorConfig.METHOD_TYPE methodType) {
        return true;
    }

    @Override
    public void validate(Product objnew, Product objold, ValidatorCallback callback) {

        /*
        if(objnew.getCenter() == null){
            callback.addMessage(GinasProcessingMessage.WARNING_MESSAGE("Center is required"));
        }
        */

        /*
        if(objnew.getAppType() == null){
            callback.addMessage(GinasProcessingMessage.WARNING_MESSAGE("null Application Type"));
        }else if(objnew.getNumber().trim().isEmpty()){
            callback.addMessage(GinasProcessingMessage.WARNING_MESSAGE("blank Application Number"));
        }
         */
    }
}
