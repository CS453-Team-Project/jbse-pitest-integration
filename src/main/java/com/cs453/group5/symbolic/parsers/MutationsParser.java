package com.cs453.group5.symbolic.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private String pitestBaseDirPath;

    public MutationsParser(String pitestBaseDirPath) {
        this.pitestBaseDirPath = pitestBaseDirPath;
    }

    public String getRecentPitestReportPath() throws InterruptedException, IOException {
        final String command = String.format("cd %s && ls -d */ | grep 20", pitestBaseDirPath);
        final ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

        String xmlDir = "";

        Process process = processBuilder.start();
        BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = outReader.readLine()) != null) {
            xmlDir = line;
        }

        process.waitFor();

        return String.format("%s/%s/mutations.xml", pitestBaseDirPath, xmlDir);
    }

    private Document getNormalizedDoc(String path) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        return doc;
    }

    private Boolean isKilledMutant(Element element) {
        String status = element.getAttribute("status");
        return status.equals("KILLED") || status.equals("RUN_ERROR");
    }

    private MutantId getMutantId(Element element) {
        String mutatedClass = element.getElementsByTagName("mutatedClass").item(0).getTextContent();
        String mutatedMethod = element.getElementsByTagName("mutatedMethod").item(0).getTextContent();
        String methodDescription = element.getElementsByTagName("methodDescription").item(0).getTextContent();
        String mutator = element.getElementsByTagName("mutator").item(0).getTextContent();

        String index_s = element.getElementsByTagName("index").item(0).getTextContent();
        String block_s = element.getElementsByTagName("block").item(0).getTextContent();
        String lineNumber_s = element.getElementsByTagName("lineNumber").item(0).getTextContent();

        int index = Integer.parseInt(index_s);
        int block = Integer.parseInt(block_s);
        int lineNumber = Integer.parseInt(lineNumber_s);

        return new MutantId(mutatedClass, mutatedMethod, methodDescription, mutator, index, block, lineNumber);
    }

    public Set<MutantId> getSurvivedMutantIds()
            throws SAXException, IOException, ParserConfigurationException, InterruptedException {
        if (xmlPath == null) {
            xmlPath = getRecentPitestReportPath();
        }

        HashSet<MutantId> mutIdSet = new HashSet<MutantId>();

        Document doc = getNormalizedDoc(xmlPath);
        NodeList list = doc.getElementsByTagName("mutation");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element element = (Element) node;

            if (isKilledMutant(element)) {
                continue;
            }

            mutIdSet.add(getMutantId(element));
        }

        return mutIdSet;
    }
}