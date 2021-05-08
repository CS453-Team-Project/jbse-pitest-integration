package com.cs453.group5.symbolic;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.cs453.group5.symbolic.entities.MutantId;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MutationsParser {
    private String xmlPath;

    public MutationsParser(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public Set<MutantId> getSurvivedMutantIds() {
        HashSet<MutantId> mutIdSet = new HashSet<MutantId>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlPath));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("mutation");
            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    /* Skip killed or crashed mutants */
                    String status = element.getAttribute("status");
                    if (status.equals("KILLED") || status.equals("RUN_ERROR")) {
                        continue;
                    }

                    /* Get mutation info */
                    String mutatedClass = element.getElementsByTagName("mutatedClass").item(0).getTextContent();
                    String mutatedMethod = element.getElementsByTagName("mutatedMethod").item(0).getTextContent();
                    String methodDescription = element.getElementsByTagName("methodDescription").item(0)
                            .getTextContent();
                    String mutator = element.getElementsByTagName("mutator").item(0).getTextContent();

                    String index_s = element.getElementsByTagName("index").item(0).getTextContent();
                    String block_s = element.getElementsByTagName("block").item(0).getTextContent();
                    String lineNumber_s = element.getElementsByTagName("lineNumber").item(0).getTextContent();

                    int index = Integer.parseInt(index_s);
                    int block = Integer.parseInt(block_s);
                    int lineNumber = Integer.parseInt(lineNumber_s);

                    /* Add it to the set */
                    mutIdSet.add(new MutantId(mutatedClass, mutatedMethod, methodDescription, mutator, index, block,
                            lineNumber));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mutIdSet;
    }
}