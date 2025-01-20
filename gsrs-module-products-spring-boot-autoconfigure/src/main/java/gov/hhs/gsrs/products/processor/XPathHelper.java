package gov.hhs.gsrs.products.processor;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

@Slf4j
public class XPathHelper {
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
        NodeList xPathNodeList = (NodeList)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODE);
        return xPathNodeList.item(0).getTextContent();
    }
    public void printElementValueByTag(Node startNode, String expression, String label) throws XPathExpressionException {
        System.out.println(label + getElementValueByTag(startNode, expression));
    }

    public String getElementValueByAttribute(Node startNode, String expression, String attributeId) {
        try {
            NodeList xPathNodeList = (NodeList) xPath.compile(expression).evaluate(startNode, XPathConstants.NODESET);
            return xPathNodeList.item(0).getAttributes().getNamedItem(attributeId).getNodeValue();
        } catch (NullPointerException e) {
            log.error("Null expression for this xml: " + expression);
        } catch (XPathExpressionException e) {
            log.error("XPath exception for this xml: " + expression);
        }

        return "na";
    }
    public void printElementValueByAttribute(Node startNode, String expression, String attributeId, String label) throws XPathExpressionException {
        System.out.println(label + getElementValueByAttribute(startNode, expression, attributeId));
    }
    public NamedNodeMap getElementAttributes(Node startNode, String expression) throws XPathExpressionException {
        NodeList xPathNodeList = (NodeList)  xPath.compile(expression).evaluate(startNode, XPathConstants.NODESET);
        return xPathNodeList.item(0).getAttributes();
    }
}
