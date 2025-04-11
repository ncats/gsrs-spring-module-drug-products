package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.processor.DailyMedXmlFileProcessor;
import gov.hhs.gsrs.products.processor.XPathHelper;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DailyMedXmlFileProcessorTests {

    @Test
    void testBasicPutIngredientFromNode() throws XPathExpressionException {
        String ourId = "ABCD";
        String ourName = "The substance";
        ImportProduct product = new ImportProduct();
        Node testNode = mock(Node.class);
        Node classCodeLabelNode = mock(Node.class);
        classCodeLabelNode.setNodeValue("classCode");
        NamedNodeMap attributeMap = mock(NamedNodeMap.class);
        when( testNode.getAttributes()).thenReturn(attributeMap);
        when(attributeMap.setNamedItem(classCodeLabelNode))
                .thenReturn(classCodeLabelNode);
        when(classCodeLabelNode.getNodeValue()).thenReturn(ourId);
        when(attributeMap.getNamedItem("classCode")).thenReturn(classCodeLabelNode);
        testNode.getAttributes().setNamedItem(classCodeLabelNode).setNodeValue("NOTACT");
        XPathHelper xph = mock(XPathHelper.class);
        when(xph.getElementValueByAttribute(testNode,".//ingredientSubstance/code", "code"))
                .thenReturn(ourId);
        Assertions.assertTrue(true);
        when(xph.getElementValueByTag(testNode, ".//ingredientSubstance/name"))
                .thenReturn(ourName);

        DailyMedXmlFileProcessor processor = new DailyMedXmlFileProcessor();
        setXPathHelper(processor, xph);
        processor.putIngredientFromNode(product, testNode);

        Assertions.assertEquals(1, product.getIngredients().size());
        Assertions.assertEquals(ourName, product.getIngredients().get(ourId).getSubstanceName());
    }

    private void setXPathHelper(DailyMedXmlFileProcessor processor, XPathHelper helper) {
        String fieldName = "initValidator";
        try {
            Field helperField = processor.getClass().getDeclaredField("xph");
            helperField.setAccessible(true);
            helperField.set(processor, helper);
        } catch (NoSuchFieldException e) {
            log.error("no field found {}", fieldName);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("access error running method {}: {}", fieldName, e);
            throw new RuntimeException(e);
        }
    }
}
