package gov.hhs.gsrs.products.product.utilities.xmlimporter;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class DailyMedXmlFileProcessor {
    // Taking each file and extract/parse each field and populating a data holder object

    public XPath xPath = XPathFactory.newInstance().newXPath();

    public XPathHelper xph = new XPathHelper();

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
                // log.error(String.valueOf(e.getCause()));
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();
            holder.productType = xph.getElementValueByAttribute(doc, ".//document/code", "displayName");

            holder.title = xph.getElementValueByTag(doc, ".//document/title");

            holder.effectiveTime = xph.getElementValueByAttribute(doc, ".//document/effectiveTime", "value");

            holder.setId = xph.getElementValueByAttribute(doc, ".//document/setId", "root");
            System.out.println("SETID: "+ holder.setId);

            System.out.println("SETID: https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=" + holder.setId);

            holder.versionId = xph.getElementValueByAttribute(doc, ".//document/versionNumber", "value");

            holder.manufacturerName = xph.getElementValueByTag(doc, ".//document/author/assignedEntity/representedOrganization/name");

            holder.manufacturerCode = xph.getElementValueByAttribute(doc, ".//document/author/assignedEntity/representedOrganization/id", "extension");

            //holder.assignedOrganizationName = xph.getElementValueByTag(doc, ".//document/author/assignedEntity/representedOrganization/assignedEntity/assignedOrganization/name");

            holder.representedOrganizationIdExtension = xph.getElementValueByAttribute(doc, ".//document/author/assignedEntity/representedOrganization/id", "extension");

            //holder.duns2 = xph.getElementValueByAttribute(doc, ".//document/author/assignedEntity/representedOrganization//assignedEntity/assignedOrganization/id", "extension");

            NodeList productsNodeList1 = xph.getNodeListByTag(doc, ".//document/component/structuredBody/component/*/subject/*");
            for (int z = 0; z < productsNodeList1.getLength(); z++) {
                Node pNode = productsNodeList1.item(z);
                putProductFromNode(holder, pNode);
            }

            // products2 ?? not doing anything yet
            NodeList productsNodeList2 = xph.getNodeListByTag(doc,"/document/author/assignedEntity/representedOrganization/assignedEntity/assignedOrganization/assignedEntity/*/actDefinition/product/manufacturedProduct/manufacturedMaterialKind");
            for (int i = 0; i < productsNodeList2.getLength(); i++) {
                Node node = productsNodeList2.item(i);
            }

        } catch (Exception e) {
            // log.error(String.valueOf(e.getCause()));
            e.printStackTrace();
        }
        System.out.println("done");
        return holder;

    }
    //processing ends here

    public void putProductFromNode(DailyMedXmlFileDataHolder holder, Node pNode) throws XPathExpressionException {

        String code = xph.getElementValueByAttribute(pNode, ".//manufacturedProduct/code", "code");

        if (code != null) {
            Product p = holder.getProduct(code);


            if(p==null) {

                holder.products.put(code, new Product());
                p = holder.getProduct(code);

            }
            p.productName = xph.getElementValueByTag(pNode, ".//manufacturedProduct/name");
            p.genericName = xph.getElementValueByTag(pNode, ".//manufacturedProduct/asEntityWithGeneric/genericMedicine/name");
            p.productType = holder.productType;
            p.title = holder.title;
            p.effectiveTime = holder.effectiveTime;
            p.setId = holder.setId;
            p.versionId = holder.versionId;
            p.manufacturerCode= holder.manufacturerCode;
            p.manufacturerName = holder.manufacturerName;
            p.companyName= holder.manufacturerName;
            p.assignedOrganizationName = holder.assignedOrganizationName;
            p.representedOrganizationIdExtension = holder.representedOrganizationIdExtension;
            p.duns2 = holder.duns2;

            p.productCode = code;

            p.formCode = xph.getElementValueByAttribute(pNode, ".//manufacturedProduct/formCode", "displayName");
            p.routeCode = xph.getElementValueByAttribute(pNode, ".//consumedIn/substanceAdministration/routeCode", "displayName");
            p.packagedFormCode = xph.getElementValueByAttribute(pNode, ".//asContent/containerPackagedProduct/formCode", "displayName");
            p.productStatus = xph.getElementValueByAttribute(pNode, ".//asContent/*/marketingAct/statusCode", "code");
            p.marketingDateLow = xph.getElementValueByAttribute(pNode, ".//asContent/*/marketingAct/effectiveTime/low", "value");
            // * is subjectOf
            p.fdaApprovalId = xph.getElementValueByAttribute(pNode, ".//*/approval/id", "extension");
            p.fdaApplicationType = xph.getElementValueByAttribute(pNode, ".//*/approval/code", "displayName");
            NodeList characteristicNodeList = xph.getNodeListByTag(pNode, ".//*/characteristic");

            for (int y = 0; y < characteristicNodeList.getLength(); y++) {
                Node cNode = characteristicNodeList.item(y);
                processCharacteristicFromNode(p, cNode);
            }

            NodeList ingredientNodeList = xph.getNodeListByTag(pNode, ".//ingredient");
            for (int y = 0; y < ingredientNodeList.getLength(); y++) {
                Node iNode = ingredientNodeList.item(y);
                putIngredientFromNode(y, p, iNode);
            }
        }
    }

    public void processCharacteristicFromNode(Product p, Node cNode) throws XPathExpressionException {
        String code = xph.getElementValueByAttribute(cNode, ".//code", "code");
        if (code.equals("SPLIMPRINT")) {
            p.splImprintValue = xph.getElementValueByTag(cNode, ".//value");
        }
        else if (code.equals("SPLCOLOR")) {
            p.splColorValue = xph.getElementValueByAttribute(cNode, ".//value", "displayName");
        }
        else if (code.equals("SPLSHAPE")) {
            p.splShapeValue = xph.getElementValueByAttribute(cNode, ".//value", "displayName");
        }
        else if (code.equals("SPLSIZE")) {
            Node sizeNode = xph.getNodeByTag(cNode, ".//value");
            String value = sizeNode.getAttributes().getNamedItem("value").getNodeValue();
            String unit = sizeNode.getAttributes().getNamedItem("unit").getNodeValue();
            p.splSizeValue = value + unit;
        }
    }

    public void putIngredientFromNode(int ic, Product p, Node iNode) throws XPathExpressionException {
        String classCode = iNode.getAttributes().getNamedItem("classCode").getNodeValue();
        String uniiCode = xph.getElementValueByAttribute(iNode, ".//ingredientSubstance/code", "code");
        if (uniiCode != null)  {
            //Ingredient i = p.getIngredient(uniiCode);
            if (uniiCode.equals("NOT FOUND")) {
                // Per phone conv. with Frank, Do not add ingredient if missing a UNII.
                return;
                // uniiCode = "NOT_FOUND_" + ic;
            }
                Ingredient i = p.getIngredient(uniiCode);
                if (i == null) {
                    p.ingredients.put(uniiCode, new Ingredient());
                    i = p.getIngredient(uniiCode);
                }
                i.uniiCode = uniiCode;
                i.substanceName = xph.getElementValueByTag(iNode, ".//ingredientSubstance/name");
                i.classCode = classCode;
                if (classCode != null && classCode.startsWith("ACT")) {
                    Node numeratorNode = xph.getNodeByTag(iNode, "quantity/numerator");
                    i.numerator = numeratorNode.getAttributes().getNamedItem("value").getNodeValue();
                    i.numeratorUnit = numeratorNode.getAttributes().getNamedItem("unit").getNodeValue();
                    Node denominatorNode = xph.getNodeByTag(iNode, "quantity/denominator");
                    i.denominator = denominatorNode.getAttributes().getNamedItem("value").getNodeValue();
                    i.denominatorUnit = denominatorNode.getAttributes().getNamedItem("unit").getNodeValue();
                    if (classCode.equals("ACTIB") || classCode.equals("ACTIM")) {
                        i.basisOfStrengthCode = xph.getElementValueByAttribute(iNode, ".//ingredientSubstance/activeMoiety/activeMoiety/code", "code");
                        i.basisOfStrengthName = xph.getElementValueByTag(iNode, ".//ingredientSubstance/activeMoiety/activeMoiety/name");
                    } else if (classCode.equals("ACTIR")) {
                        i.basisOfStrengthCode = xph.getElementValueByAttribute(iNode, ".//ingredientSubstance/asEquivalentSubstance/definingSubstance/code", "code");
                        i.basisOfStrengthName = xph.getElementValueByTag(iNode, ".//ingredientSubstance/asEquivalentSubstance/definingSubstance/name");
                    }

            }
        }
    }
}


class DailyMedXmlFileDataHolder {
    String productType;
    String title;
    String effectiveTime;
    String setId;
    String versionId;
    String manufacturerCode;
    String manufacturerName;
    String assignedOrganizationName;
    String representedOrganizationIdExtension;
    String duns2;

    Map<String, Product> products = new HashMap<String, Product>();

    public Product getProduct(String key) {
        return this.products.get(key);
    }

    public void putProduct(String key, Product product) {
        this.products.put(key, product);
    }
}

class Product {
    String productType;
    String title;
    String effectiveTime;
    String setId;
    String versionId;
    String manufacturerCode;
    String manufacturerName;
    String assignedOrganizationName;
    String representedOrganizationIdExtension;
    String duns2;

    String ndcCode;
    String productCode;
    String productName;
    String companyName;
    String genericName;
    String formCode;
    String routeCode;
    String packagedFormCode;
    String productStatus;
    String marketingDateLow;
    String fdaApprovalId;
    String fdaApplicationType;
    String splImprintValue;
    String splColorValue;
    String splShapeValue;
    String splSizeValue;

    Map<String, Ingredient> ingredients = new HashMap<String, Ingredient>();

    public Ingredient getIngredient(String key) {
        return this.ingredients.get(key);
    }

    public void putIngredient(String key, Ingredient ingredient) {
        this.ingredients.put(key, ingredient);
    }
}

class Ingredient {
    String substanceName;
    String classCode;
    String uniiCode;
    String uuid;
    String numerator;
    String numeratorUnit;
    String denominator;
    String denominatorUnit;
    String basisOfStrengthCode;
    String basisOfStrengthName;
}
@Slf4j

class XPathHelper {

    public XPath xPath;

    public void setXpath(XPath xPath) {
       this.xPath = xPath;
    }
    public Node getNodeByTag(Node startNode, String expression) throws XPathExpressionException {
        return (Node)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODE);
    }

    public NodeList getNodeListByTag(Node startNode, String expression) throws XPathExpressionException {
        return (NodeList)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODESET);
    }
    public String getElementValueByTag(Node startNode, String expression) throws XPathExpressionException {
        try {
            NodeList xPathNodeList = (NodeList)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODE);
            return xPathNodeList.item(0).getTextContent();
        } catch (NullPointerException e) {
            log.error("Null expression for this xml: " + expression);
        } catch (XPathExpressionException e) {
            log.error("XPath exception for this xml: " + expression);
        }
        return "NOT FOUND";
    }
    public void printElementValueByTag(Node startNode, String expression, String label) throws XPathExpressionException {
        System.out.println(label + getElementValueByTag(startNode, expression));
    }
    public String _getElementValueByAttribute(Node startNode, String expression, String attributeId) throws XPathExpressionException {
        try {
            NodeList xPathNodeList = (NodeList)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODESET);
            return xPathNodeList.item(0).getAttributes().getNamedItem(attributeId).getNodeValue();
        } catch (NullPointerException e) {
            log.error("Null expression for this xml: " + expression);
        } catch (XPathExpressionException e) {
            log.error("XPath exception for this xml: " + expression);
        }
        return "NOT FOUND";
    }

    public String getElementValueByAttribute(Node startNode, String expression, String attributeId) {
        try {
            NodeList xPathNodeList = (NodeList) xPath.compile(expression).evaluate(startNode, XPathConstants.NODESET);
            return xPathNodeList.item(0).getAttributes().getNamedItem(attributeId).getNodeValue();
        } catch (NullPointerException e) {
            log.error("NullPointerException for this xpath expression: " + expression);
        } catch (XPathExpressionException e) {
            log.error("XPathExpressionException for this xpath expression: " + expression);
        }

        return "NOT FOUND";
    }
    public void printElementValueByAttribute(Node startNode, String expression, String attributeId, String label) throws XPathExpressionException {
        System.out.println(label + getElementValueByAttribute(startNode, expression, attributeId));
    }
    public NamedNodeMap getElementAttributes(Node startNode, String expression) throws XPathExpressionException {
        NodeList xPathNodeList = (NodeList)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODESET);
        return xPathNodeList.item(0).getAttributes();
    }
}
