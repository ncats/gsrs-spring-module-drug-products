package gov.hhs.gsrs.products.processor;

import gov.hhs.gsrs.products.processor.model.DailyMedXmlFileDataHolder;
import gov.hhs.gsrs.products.processor.model.ImportIngredient;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import gov.hhs.gsrs.products.product.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.base.Sys;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/*
Code originally written by Aruna Nishtala
*/
@Slf4j
public class DailyMedXmlFileProcessor {
    // Taking each file and extract/parse each field and populating a data holder object

    public XPath xPath = XPathFactory.newInstance().newXPath();

    public XPathHelper xph = new XPathHelper();
    
    public final String DISPLAY_NAME = "displayName";

    public DailyMedXmlFileDataHolder process(String path) {
    // This gathers all information from xml to populate a data holder object and returns the object (see DailyMedXmlFilesProcessor.java)
        xph.setXpath(xPath);

        DailyMedXmlFileDataHolder holder = new DailyMedXmlFileDataHolder();

        try {
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }

            Document doc = null;
            try {
                doc = dBuilder.parse(inputFile);
            } catch (SAXException | IOException e) {
                log.error("Error during parsing ", e);
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();
            holder.setProductType(xph.getElementValueByAttribute(doc, ".//document/code", DISPLAY_NAME));
            holder.setTitle(xph.getElementValueByTag(doc, ".//document/title"));
            holder.setEffectiveTime(xph.getElementValueByAttribute(doc, ".//document/effectiveTime", "value"));
            holder.setSetId(xph.getElementValueByAttribute(doc, ".//document/setId", "root"));
            log.trace("SETID: {}", holder.getSetId());
            log.trace("SETID: https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=" + holder.getSetId());

            holder.setVersionId(xph.getElementValueByAttribute(doc, ".//document/versionNumber", "value"));
            holder.setManufacturerName(xph.getElementValueByTag(doc, ".//document/author/assignedEntity/representedOrganization/name"));
            holder.setManufacturerCode(xph.getElementValueByAttribute(doc, ".//document/author/assignedEntity/representedOrganization/id", 
                    "extension"));
            holder.setRepresentedOrganizationIdExtension(xph.getElementValueByAttribute(doc, ".//document/author/assignedEntity/representedOrganization/id", "extension"));

            NodeList productsNodeList1 = xph.getNodeListByTag(doc, ".//document/component/structuredBody/component/*/subject/*");
            for (int z = 0; z < productsNodeList1.getLength(); z++) {
                Node pNode = productsNodeList1.item(z);
                putProductFromNode(holder, pNode);
            }

            //removed loop that was doing nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("done");
        return holder;
    }

    public void putProductFromNode(DailyMedXmlFileDataHolder holder, Node pNode) throws XPathExpressionException {

        String code = xph.getElementValueByAttribute(pNode, ".//manufacturedProduct/code", "code");

        if (code != null) {
            ImportProduct p = holder.getProduct(code);


            if(p==null) {

                holder.getProducts().put(code, new ImportProduct());
                p = holder.getProduct(code);

            }
            p.setProductName(xph.getElementValueByTag(pNode, ".//manufacturedProduct/name"));
            p.setGenericName(xph.getElementValueByTag(pNode, ".//manufacturedProduct/asEntityWithGeneric/genericMedicine/name"));
            p.setProductType( holder.getProductType());
            p.setTitle(holder.getTitle());
            p.setEffectiveTime(holder.getEffectiveTime());
            p.setSetId(holder.getSetId());
            p.setVersionId(holder.getVersionId());
            p.setManufacturerCode(holder.getManufacturerCode());
            p.setManufacturerName(holder.getManufacturerName());
            p.setCompanyName(holder.getManufacturerName());
            p.setAssignedOrganizationName(holder.getAssignedOrganizationName());
            p.setRepresentedOrganizationIdExtension(holder.getRepresentedOrganizationIdExtension());
            p.setDuns2( holder.getDuns2());

            p.setProductCode(code);

            p.setFormCode(xph.getElementValueByAttribute(pNode, ".//manufacturedProduct/formCode", DISPLAY_NAME));
            p.setRouteCode(xph.getElementValueByAttribute(pNode, ".//consumedIn/substanceAdministration/routeCode", DISPLAY_NAME));
            p.setPackagedFormCode(xph.getElementValueByAttribute(pNode, ".//asContent/containerPackagedProduct/formCode", DISPLAY_NAME));
            p.setProductStatus(xph.getElementValueByAttribute(pNode, ".//asContent/*/marketingAct/statusCode", "code"));
            p.setMarketingDateLow(xph.getElementValueByAttribute(pNode, ".//asContent/*/marketingAct/effectiveTime/low", "value"));
            // * is subjectOf
            p.setFdaApprovalId(xph.getElementValueByAttribute(pNode, ".//*/approval/id", "extension"));
            p.setFdaApplicationType(xph.getElementValueByAttribute(pNode, ".//*/approval/code", DISPLAY_NAME));
            NodeList characteristicNodeList = xph.getNodeListByTag(pNode, ".//*/characteristic");

            for (int y = 0; y < characteristicNodeList.getLength(); y++) {
                Node cNode = characteristicNodeList.item(y);
                processCharacteristicFromNode(p, cNode);
            }

            NodeList ingredientNodeList = xph.getNodeListByTag(pNode, ".//ingredient");
            for (int y = 0; y < ingredientNodeList.getLength(); y++) {
                Node iNode = ingredientNodeList.item(y);
                putIngredientFromNode(p, iNode);
            }
        }
    }

    public void processCharacteristicFromNode(ImportProduct p, Node cNode) throws XPathExpressionException {
        String code = xph.getElementValueByAttribute(cNode, ".//code", "code");
        if (code.equals("SPLIMPRINT")) {
            p.setSplImprintValue(xph.getElementValueByTag(cNode, ".//value"));
        }
        else if (code.equals("SPLCOLOR")) {
            p.setSplColorValue(xph.getElementValueByAttribute(cNode, ".//value", DISPLAY_NAME));
        }
        else if (code.equals("SPLSHAPE")) {
            p.setSplShapeValue(xph.getElementValueByAttribute(cNode, ".//value", DISPLAY_NAME));
        }
        else if (code.equals("SPLSIZE")) {
            Node sizeNode = xph.getNodeByTag(cNode, ".//value");
            String value = sizeNode.getAttributes().getNamedItem("value").getNodeValue();
            String unit = sizeNode.getAttributes().getNamedItem("unit").getNodeValue();
            p.setSplSizeValue(value + unit);
        }
    }

    public void putIngredientFromNode(ImportProduct p, Node iNode) throws XPathExpressionException {
        String classCode = iNode.getAttributes().getNamedItem("classCode").getNodeValue();
        String uniiCode = xph.getElementValueByAttribute(iNode, ".//ingredientSubstance/code", "code");
        if (uniiCode != null && uniiCode.length()> 0) {
            ImportIngredient i = p.getIngredient(uniiCode);
            if (i == null) {
                p.getIngredients().put(uniiCode, new ImportIngredient());
                i = p.getIngredient(uniiCode);
            }
            i.setUniiCode(uniiCode);
            i.setSubstanceName(xph.getElementValueByTag(iNode, ".//ingredientSubstance/name"));
            i.setClassCode(classCode);

            if (classCode != null && classCode.startsWith("ACT")) {
                Node numeratorNode = xph.getNodeByTag(iNode, "quantity/numerator");
                i.setNumerator(numeratorNode.getAttributes().getNamedItem("value").getNodeValue());
                i.setNumeratorUnit(numeratorNode.getAttributes().getNamedItem("unit").getNodeValue());
                Node denominatorNode = xph.getNodeByTag(iNode, "quantity/denominator");
                i.setDenominator(denominatorNode.getAttributes().getNamedItem("value").getNodeValue());
                i.setDenominatorUnit(denominatorNode.getAttributes().getNamedItem("unit").getNodeValue());
                if (classCode.equals("ACTIB") || classCode.equals("ACTIM")) {
                    i.setBasisOfStrengthCode(xph.getElementValueByAttribute(iNode, ".//ingredientSubstance/activeMoiety/activeMoiety/code", "code"));
                    i.setBasisOfStrengthName(xph.getElementValueByTag(iNode, ".//ingredientSubstance/activeMoiety/activeMoiety/name"));
                } else if (classCode.equals("ACTIR")) {
                    i.setBasisOfStrengthCode(xph.getElementValueByAttribute(iNode, ".//ingredientSubstance/asEquivalentSubstance/definingSubstance/code", "code"));
                    i.setBasisOfStrengthName(xph.getElementValueByTag(iNode, ".//ingredientSubstance/asEquivalentSubstance/definingSubstance/name"));
                }
            }
        }
    }
}
