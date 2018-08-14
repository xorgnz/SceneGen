package org.memehazard.bonus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.memehazard.wheel.query.parser.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TalairachParser
{
    public static final String PATH  = "D:\\work\\www\\sig\\SG-braindata\\";
    public static final String[] FILENAMES = {"temporal", "occipital", "frontal", "prefrontal"};

    
    

    public List<TalairachMarker> parse(Reader reader) throws IOException, ParserConfigurationException, SAXException
    {
        // Initialize XML parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(new InputSource(reader));

        return parseDocument(doc);
    }


    private List<TalairachMarker> parseDocument(Document doc)
    {
        // Check for valid root node
        Node root = doc.getFirstChild();

        // Initialize data structures
        List<TalairachMarker> markers = new ArrayList<TalairachMarker>();

        // Parse nodes
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node n = childNodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("patient".equals(n.getNodeName()))
                    markers.addAll(this.parsePatientNode(n));
            }
        }
        return markers;

    }


    private Collection<TalairachMarker> parsePatientNode(Node n)
    {
        List<TalairachMarker> markers = new ArrayList<TalairachMarker>();

        // Parse nodes
        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("site".equals(child.getNodeName()))
                    markers.add(parseSiteNode(child));
            }
        }

        return markers;
    }


    private TalairachMarker parseSiteNode(Node n)
    {
        TalairachMarker marker = new TalairachMarker();

        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("right_coord".equals(child.getNodeName()))
                    marker.setX(Double.parseDouble(parseValueNode(child)));
                else if ("sup_coord".equals(child.getNodeName()))
                    marker.setY(Double.parseDouble(parseValueNode(child)));
                else if ("ant_coord".equals(child.getNodeName()))
                    marker.setZ(Double.parseDouble(parseValueNode(child)));
                else if ("Tal".equals(child.getNodeName()))
                    marker.setLabel(parseValueNode(child));
                else if ("Color".equals(child.getNodeName()))
                    marker.setColor(parseValueNode(child));
            }
        }

        return marker;
    }


    private String parseValueNode(Node n)
    {
        StringBuilder sb = new StringBuilder();

        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE)
                sb.append(child.getNodeValue());
        }

        return sb.toString();
    }


    public static void main(String[] argv) throws IOException, ParserException
    {
        try
        {
            for (String fn : FILENAMES)
            {
                FileReader f = new FileReader(new File(PATH + fn + ".xml"));
                TalairachParser parser = new TalairachParser();
                Collection<TalairachMarker> markers = parser.parse(f);
    
                FileWriter fw = new FileWriter(new File(PATH + fn + ".js"));
    
                fw.append("var markers = [");
                for (TalairachMarker tm : markers)
                    fw.append(tm.toJSString() + ",");
                fw.append("];");
                
                fw.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
