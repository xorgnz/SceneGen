/**
 * 
 */
package org.memehazard.wheel.asset.codec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.memehazard.wheel.asset.model.Mesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class X3DCodec
{
    private Logger log = LoggerFactory.getLogger(X3DCodec.class);


    public Mesh decode(File f) throws CodecException, IOException
    {
        return decode(new BufferedReader(new FileReader(f)));
    }


    public Mesh decode(String s) throws CodecException, IOException
    {
        return decode(new StringReader(s));
    }


    public Mesh decode(Reader r) throws CodecException, IOException
    {
        try
        {
            // Initialize XML parser
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new InputSource(r));
            NodeList childNodes = doc.getChildNodes();

            // Count instances of IndexedFaceSet
            List<Node> ifsNodes = null;
            for (int i = 0; i < childNodes.getLength(); i++)
            {
                Node n = childNodes.item(i);

                // Process document
                if (n.getNodeType() == Node.ELEMENT_NODE)
                {
                    if ("X3D".equalsIgnoreCase(n.getLocalName()))
                        ifsNodes = findIndexedFaceSetNodes(n);
                    else
                        throw new CodecException("X3D file is invalid - has incorrect root node: " + n.getLocalName());
                }
            }

            // Check result
            if (ifsNodes.size() == 0)
                throw new CodecException("X3D file contains no mesh. Cannot parse X3D files based solely on primitives");
            else if (ifsNodes.size() > 1)
                throw new CodecException("X3D file contains multiple meshes. Only single mesh files allowed");

            Mesh mesh = null;
            for (Node n : ifsNodes)
            {
                mesh = parseIndexedFaceSetNode(n);
            }

            return mesh;
        }
        catch (ParserConfigurationException pce)
        {
            throw new CodecException("XML Parser improperly configured", pce);
        }
        catch (SAXException saxe)
        {
            throw new CodecException("XML document fails SAX parsing", saxe);
        }
        finally
        {
            r.close();
        }
    }


    public List<Node> findIndexedFaceSetNodes(Node n)
    {
        List<Node> ifsNodes = new ArrayList<Node>();

        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("IndexedFaceSet".equals(child.getLocalName()))
                    ifsNodes.add(child);
                else
                    ifsNodes.addAll(findIndexedFaceSetNodes(child));
            }
        }

        return ifsNodes;
    }


    /**
     * Recursively iterate through nodes to find a Coordinate node. Once found, parse it to create result object. Skip
     * parsing future nodes once result object is found.
     * 
     * @param n Current node to parse
     * @return Result statistics, if found
     */
    private Mesh parseIndexedFaceSetNode(Node n) throws CodecException
    {
        Mesh mesh = new Mesh();

        // Extract face coordinate strings
        NamedNodeMap atts = n.getAttributes();
        String coordIndex = atts.getNamedItem("coordIndex").getNodeValue();
        coordIndex = coordIndex.replace(',', ' ');
        String[] faceStrings = StringUtils.splitByWholeSeparator(coordIndex, "-1");

        // Generate faces from each face string
        for (int i = 0; i < faceStrings.length; i++)
        {
            String[] coordStrings = StringUtils.split(faceStrings[i]);
            if (coordStrings.length == 3)
                mesh.addFace(
                        Integer.parseInt(coordStrings[0]),
                        Integer.parseInt(coordStrings[1]),
                        Integer.parseInt(coordStrings[2]));
            else if (coordStrings.length == 4)
                mesh.addFacesFromQuad(
                        Integer.parseInt(coordStrings[0]),
                        Integer.parseInt(coordStrings[1]),
                        Integer.parseInt(coordStrings[2]),
                        Integer.parseInt(coordStrings[3]));
            else
                log.error("Ignoring face with " + coordStrings.length + " vertices.");
        }

        // Extract vertices
        NodeList childNodes = n.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            Node child = childNodes.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("Coordinate".equals(child.getLocalName()))
                {
                    NamedNodeMap atts2 = child.getAttributes();

                    String vertices = atts2.getNamedItem("point").getNodeValue();
                    vertices = vertices.replace(',', ' ');
                    String[] vertexNums = StringUtils.split(vertices);

                    // Grab each set of three numbers and generate a vertex
                    for (int j = 0; j * 3 + 2 < vertexNums.length; j++)
                    {
                        double x = Double.parseDouble(vertexNums[j * 3]);
                        double y = Double.parseDouble(vertexNums[j * 3 + 1]);
                        double z = Double.parseDouble(vertexNums[j * 3 + 2]);

                        mesh.addVertex(x, y, z);
                    }
                }
            }
        }

        if (!mesh.isValid())
            throw new CodecException("Cannot parse X3D file. Invalid IndexedFaceSet found");

        return mesh;
    }


    public void encode(File f, Mesh data) throws CodecException, IOException
    {
        FileWriter fw = new FileWriter(f);
        try
        {
            // Create the document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document d = db.newDocument();

            // Set up the root element and necessary namespaces
            Element n_root = d.createElement("X3D");
            Element n_scene = d.createElement("Scene");
            Element n_shape = d.createElement("Shape");

            // Create and append the IndexedFaceSet node
            n_shape.appendChild(buildIndexedFaceSetNode(d, data));
            n_shape.appendChild(buildAppearanceNode(d));

            n_scene.appendChild(n_shape);
            n_root.appendChild(n_scene);
            d.appendChild(n_root);

            // Write the resulting document to disk
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");

            t.transform(new DOMSource(d), new StreamResult(fw));

        }
        catch (TransformerException te)
        {
            throw new CodecException("Unable to serialize XML DOM", te);
        }
        catch (ParserConfigurationException pce)
        {
            throw new CodecException("XML Parser improperly configured", pce);
        }
        finally
        {
            fw.close();
        }
    }


    /**
     * @param data
     * @return
     */
    private Node buildIndexedFaceSetNode(Document d, Mesh data)
    {
        Element n_ifs = d.createElement("IndexedFaceSet");

        n_ifs.setAttribute("creaseAngle", "1.57");
        n_ifs.setAttribute("solid", "false");
        n_ifs.setAttribute("coordIndex", StringUtils.join(data.getFaceData(), " -1, "));

        n_ifs.appendChild(buildCoordinateNode(d, data));

        return n_ifs;
    }


    /**
     * @param d
     * @param data
     * @return
     */
    private Node buildCoordinateNode(Document d, Mesh data)
    {
        Element n = d.createElement("Coordinate");

        n.setAttribute("point", StringUtils.join(data.getVertexData(), ", "));

        return n;
    }


    private Node buildAppearanceNode(Document d)
    {
        Element n_appr = d.createElement("Appearance");
        Element n_mtl = d.createElement("Material");

        n_mtl.setAttribute("diffuseColor", "1.0 1.0 1.0");
        n_mtl.setAttribute("specularColor", "0 0 0");
        n_mtl.setAttribute("emissiveColor", "0 0 0");
        n_mtl.setAttribute("ambientIntensity", "1.0");
        n_mtl.setAttribute("shininess", "0.5");
        n_mtl.setAttribute("transparency", "0.0");

        n_appr.appendChild(n_mtl);
        return n_appr;
    }
}