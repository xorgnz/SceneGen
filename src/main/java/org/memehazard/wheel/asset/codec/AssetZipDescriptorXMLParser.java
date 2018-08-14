package org.memehazard.wheel.asset.codec;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.memehazard.exceptions.XMLException;
import org.memehazard.wheel.asset.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AssetZipDescriptorXMLParser
{
    private Logger log = LoggerFactory.getLogger(this.getClass());


    public Map<String, AssetZipDescriptor> parse(Reader r) throws IOException, XMLException
    {
        try
        {
            // Initialize XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new InputSource(r));

            // Check for valid root node
            Node root = doc.getFirstChild();
            if (!"descriptors".equalsIgnoreCase(root.getLocalName()))
            {
                throw new XMLException("Invalid descriptors file - root node is " + root.getLocalName() + " not X3D");
            }

            // Parse descriptors
            Map<String, AssetZipDescriptor> descriptors = new HashMap<String, AssetZipDescriptor>();
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++)
            {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE && "descriptor".equals(child.getLocalName()))
                {
                    parseDescriptorNode(child, descriptors);
                }
            }

            return descriptors;
        }
        catch (ParserConfigurationException pce)
        {
            throw new XMLException("XML Parser improperly configured", pce);
        }
        catch (SAXException saxe)
        {
            throw new XMLException("XML document fails SAX parsing", saxe);
        }
    }


    private void parseDescriptorNode(Node node, Map<String, AssetZipDescriptor> descriptors)
    {
        NamedNodeMap atts = node.getAttributes();

        Node n_filename = atts.getNamedItem("filename");
        Node n_name = atts.getNamedItem("name");
        Node n_entityId = atts.getNamedItem("entityId");

        if (n_filename == null || n_name == null || n_entityId == null)
            log.warn("Skipping improperly formed descriptor node - must have filename, name, and entityId attributes");
        else
        {
            String filename = n_filename.getNodeValue();

            // If no filename is provided, skip this descriptor
            if (filename != null && !filename.trim().equals(""))
            {
                // Use filename as name if none available
                String name = n_name.getNodeValue();
                if (name == null || name.trim().equals(""))
                    name = filename;

                int entityId = Asset.DEFAULT_ENTITY_ID;
                try
                {
                    entityId = Integer.parseInt(n_entityId.getNodeValue());
                }
                catch (NumberFormatException e)
                {
                    log.warn("Setting improperly formed entity ID value " + n_entityId.getNodeValue() + " for " + filename
                             + " to default value.");
                }

                descriptors.put(filename, new AssetZipDescriptor(name, entityId));
            }
            else
            {
                log.warn("Skipping improperly formed descriptor line - filename must not be blank");
            }
        }
    }
}
