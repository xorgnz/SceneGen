package org.memehazard.wheel.query.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.memehazard.wheel.query.model.Entity;
import org.memehazard.wheel.query.model.Relation;
import org.memehazard.wheel.query.model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class QueryIntegratorXmlParser implements Parser
{
    private Logger       log        = LoggerFactory.getLogger(this.getClass());
    private final String XMLNS_FMA  = "http://purl.org/sig/ont/fma/";
    private final String XMLNS_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    private final String XMLNS_RDF  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private final String XMLNS_OWL  = "http://www.w3.org/2002/07/owl#";


    public List<Entity> parseEntities(Document doc)
    {
        // Check for valid root node
        Node root = doc.getFirstChild();

        // Initialize data structures
        List<Entity> entities = new ArrayList<Entity>();

        // Parse nodes
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node n = childNodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                Entity entity = parseEntityFromNode(n);
                if (entity != null)
                    entities.add(entity);
            }
        }
        return entities;

    }


    public List<Entity> parseEntities(Reader reader) throws ParserException
    {
        try
        {
            // Initialize XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new InputSource(reader));

            return parseEntities(doc);
        }
        catch (IOException ioe)
        {
            throw new ParserException("Unable to read input", ioe);
        }
        catch (ParserConfigurationException pce)
        {
            throw new ParserException("XML Parser improperly configured", pce);
        }
        catch (SAXException saxe)
        {
            throw new ParserException("XML document fails SAX parsing", saxe);
        }
    }


    @Override
    public List<Entity> parseEntities(String s) throws ParserException
    {
        return this.parseEntities(new StringReader(s));
    }


    public List<Relationship> parseRelationships(Document doc, List<Entity> entities)
    {
        // Initialize data structures
        List<Relationship> relationships = new ArrayList<Relationship>();

        // Check for valid root node
        Node root = doc.getFirstChild();

        // Mapify entities for lookup
        Map<String, Entity> entityMap = new HashMap<String, Entity>();
        for (Entity e : entities)
            entityMap.put(e.getResourceString(), e);

        // Parse nodes
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node n = childNodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                relationships.addAll(parseRelationshipsFromNode(n, entityMap));
            }
        }
        return relationships;
    }


    public List<Relationship> parseRelationships(Reader reader) throws ParserException
    {
        try
        {
            // Initialize XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new InputSource(reader));

            List<Entity> entities = parseEntities(doc);
            return parseRelationships(doc, entities);
        }
        catch (IOException ioe)
        {
            throw new ParserException("Unable to read input", ioe);
        }
        catch (ParserConfigurationException pce)
        {
            throw new ParserException("XML Parser improperly configured", pce);
        }
        catch (SAXException saxe)
        {
            throw new ParserException("XML document fails SAX parsing", saxe);
        }
    }


    @Override
    public List<Relationship> parseRelationships(String s) throws ParserException
    {
        StringReader r = new StringReader(s);
        return parseRelationships(r);
    }


    private Entity parseEntityFromNode(Node n)
    {
        // Create fields
        Integer fmaid = null;
        String name = null;
        Map<String, Object> data = new HashMap<String, Object>();

        // Search for FMAID and name
        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("FMAID".equalsIgnoreCase(child.getLocalName()) && null == child.getNamespaceURI() ||
                    "FMAID".equalsIgnoreCase(child.getLocalName()) && XMLNS_FMA.equals(child.getNamespaceURI()))
                {
                    fmaid = Integer.parseInt(child.getFirstChild().getNodeValue());
                }
                else if ("name".equals(child.getLocalName()) && null == child.getNamespaceURI() ||
                         "name".equals(child.getLocalName()) && XMLNS_FMA.equals(child.getNamespaceURI()) ||
                         "label".equals(child.getLocalName()) && null == child.getNamespaceURI() ||
                         "label".equals(child.getLocalName()) && XMLNS_RDFS.equals(child.getNamespaceURI()))
                {
                    name = child.getFirstChild().getNodeValue();
                }
                else if (child.hasChildNodes())
                {
                    data.put(child.getLocalName(), child.getFirstChild().getNodeValue());
                }

                // TODO - Adjust XmlParser.parseEntityFromNode to parse data.
                // TODO - Adjust tests for XmlParser.parseEntityFromNode
            }
        }

        // Attempt to create entity
        if (fmaid != null && name != null)
        {
            Node att_about = n.getAttributes().getNamedItem("rdf:about");
            if (att_about != null)
            {
                Entity e = new Entity(att_about.getNodeValue(), fmaid, name);
                e.setData(data);
                log.trace("Parsed Entity {}", e);
                return e;
            }
            else
            {
                Entity e = new Entity("No resource string", fmaid, name);
                e.setData(data);
                log.trace("Parsed Entity {}", e);
                return e;
            }
        }

        // If no entity can be created from this node, return null
        return null;
    }


    private List<Relationship> parseRelationshipsFromNode(Node n, Map<String, Entity> entities)
    {
        // Initialize data structures
        List<Relationship> relationships = new ArrayList<Relationship>();

        // Get subject entity
        Entity e_subj = null;
        Node att_subj = n.getAttributes().getNamedItem("rdf:about");
        if (att_subj != null)
            e_subj = entities.get(att_subj.getNodeValue());

        // Proceed only if subject entity exists, warn otherwise
        if (e_subj == null)
            log.warn("XML rNode refers to unknown subject entity" + n.getNodeValue());
        else
        {
            // Search child nodes for ones representing relationships.
            Node child = n.getChildNodes().item(0);
            while (child != null)
            {
                if (child.getNodeType() == Node.ELEMENT_NODE)
                {
                    // Proceed if child node is not an excluded type
                    if (!("FMAID".equalsIgnoreCase(child.getLocalName()) && null == child.getNamespaceURI()) &&
                        !("FMAID".equalsIgnoreCase(child.getLocalName()) && XMLNS_FMA.equals(child.getNamespaceURI())) &&
                        !("name".equals(child.getLocalName()) && null == child.getNamespaceURI()) &&
                        !("name".equals(child.getLocalName()) && XMLNS_FMA.equals(child.getNamespaceURI())) &&
                        !("label".equals(child.getLocalName()) && null == child.getNamespaceURI()) &&
                        !("label".equals(child.getLocalName()) && XMLNS_RDFS.equals(child.getNamespaceURI())) &&
                        !(XMLNS_OWL.equals(child.getNamespaceURI())) &&
                        !("type".equals(child.getLocalName()) && XMLNS_RDF.equals(child.getNamespaceURI())))
                    {
                        // If a child node points to a resource and has no data content, treat it as a relationship node
                        if (!child.hasChildNodes() && child.getAttributes().getNamedItem("rdf:resource") != null)
                        {
                            Entity e_obj = entities.get(child.getAttributes().getNamedItem("rdf:resource").getNodeValue());

                            // Proceed only if object entity exists, warn otherwise
                            if (e_obj == null)
                                log.warn("XML Node refers to unknown object entity" + n.getNodeValue());
                            else
                            {
                                // Create relationship
                                Relation r = new Relation(child.getNamespaceURI() + child.getLocalName());
                                relationships.add(new Relationship(e_subj, r, e_obj));

                                log.trace("Parsed Relationship {}", new Relationship(e_subj, r, e_obj));
                            }
                        }
                    }
                }

                // Get next node
                child = child.getNextSibling();
            }
        }

        return relationships;
    }
}