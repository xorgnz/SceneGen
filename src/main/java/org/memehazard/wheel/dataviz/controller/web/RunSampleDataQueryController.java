package org.memehazard.wheel.dataviz.controller.web;
//package org.memehazard.wheel.dataviz.controller;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.apache.commons.lang.text.StrBuilder;
//import org.memehazard.exceptions.XMLException;
//import org.memehazard.wheel.asset.facade.AssetFacade;
//import org.memehazard.wheel.asset.model.Asset;
//import org.memehazard.wheel.asset.model.AssetSet;
//import org.memehazard.wheel.query.facade.QueryDispatchFacade;
//import org.memehazard.wheel.query.facade.SpecialQueryRegistry.SpecialQuery;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//@Controller
//@RequestMapping("/sandbox/dataQuery")
//public class RunSampleDataQueryController
//{
//
//    @Autowired
//    private AssetFacade         facade_asset;
//
//    @Autowired
//    private QueryDispatchFacade facade_queryDispatch;
//
//    private Logger              log = LoggerFactory.getLogger(this.getClass());
//
//
//    @RequestMapping(method = RequestMethod.GET)
//    @Transactional(readOnly = true)
//    public String page(
//            Model model,
//            HttpServletRequest request)
//            throws IOException, XMLException
//    {
//        log.trace("Generating page - " + request.getServletPath());
//
//        // Act
//        AssetSet set = facade_asset.findAssetSetByName("AAL - Brain");
//        List<Asset> assets = set.getAssets();
//
//
//        ResultXmlParser parser = new ResultXmlParser();
//        StrBuilder sb = new StrBuilder();
//
//        for (Asset a : assets)
//        {
//            log.trace("\n\n");
//            log.trace("Looking at " + a, toString());
//            Map<String, String> parameters = new HashMap<String, String>();
//            parameters.put("args", "fma:" + a.getName().replace(" ", "_"));
//            String response = facade_queryDispatch.retrieveSpecialQueryResponse(SpecialQuery.SANDBOX_DATA_QUERY, parameters);
//
//            Map<String, Integer> result = parser.parse(response);
//
//            log.trace(response);
//            sb.appendln(a.getEntityId() + ", " + a.getName() + ", " + result.get("healthy") + ", " + result.get("schizo"));
//        }
//
//        log.trace(sb.toString());
//
//        // Respond
//        model.addAttribute("pageTitle", "Data Query (188) with AAL");
//        model.addAttribute("pageFile", "sandbox/output_dump");
//        return "admin/base";
//    }
//
//
//    public class ResultXmlParser
//    {
//        public Map<String, Integer> parse(String s)
//                throws IOException, XMLException
//        {
//            StringReader reader = new StringReader(s);
//            Map<String, Integer> result = new HashMap<String, Integer>();
//
//            try
//            {
//                // Initialize XML parser
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                factory.setNamespaceAware(true);
//                DocumentBuilder db = factory.newDocumentBuilder();
//                Document doc = db.parse(new InputSource(reader));
//
//                // Check for valid root node
//                Node root = doc.getFirstChild();
//
//                // Parse nodes
//                NodeList childNodes = root.getChildNodes();
//                for (int i = 0; i < childNodes.getLength(); i++)
//                {
//                    Node n = childNodes.item(i);
//                    if (n.getNodeType() == Node.ELEMENT_NODE && n.getLocalName().equals("counts"))
//                    {
//                        log.trace("in counts");
//                        NodeList grandchildNodes = n.getChildNodes();
//                        for (int j = 0; j < grandchildNodes.getLength(); j++)
//                        {
//                            Node n2 = grandchildNodes.item(j);
//                            if (n2.getNodeType() == Node.ELEMENT_NODE && n2.getLocalName().equals("healthy"))
//                            {
//                                log.trace("Found count / healthy " + n2.getTextContent());
//                                result.put("healthy", Integer.parseInt(n2.getTextContent()));
//                            }
//                            else if (n2.getNodeType() == Node.ELEMENT_NODE && n2.getLocalName().equals("schizo"))
//                            {
//                                log.trace("Found count / schizo " + n2.getTextContent());
//                                result.put("schizo", Integer.parseInt(n2.getTextContent()));
//                            }
//                            else
//                                log.trace("inner - skipping " + n2.getLocalName());
//                        }
//                    }
//                    else
//                        log.trace("Skipping " + n.getLocalName());
//                }
//            }
//            catch (ParserConfigurationException pce)
//            {
//                throw new XMLException("XML Parser improperly configured", pce);
//            }
//            catch (SAXException saxe)
//            {
//                throw new XMLException("XML document fails SAX parsing", saxe);
//            }
//
//            return result;
//        }
//    }
//}
