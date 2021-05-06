package com.cs453.group5.examples;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class ReadXmlDomParser {
  private static String FILENAME = "/root/jbse-pitest-integration/target/pit-reports/%s/mutations.xml";
  private static String DEFAULT = "/root/jbse-pitest-integration/res/default.xml";

  public static void main(String[] args) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      // parse XML file
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new File(args.length > 0 ? String.format(FILENAME, args[0]) : DEFAULT));

      // optional, but recommended
      doc.getDocumentElement().normalize();
      System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
      System.out.println("------");

      // get <staff>
      NodeList list = doc.getElementsByTagName("mutation");
      for (int temp = 0; temp < list.getLength(); temp++) {
        Node node = list.item(temp);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;

          // get status of mutant
          String status = element.getAttribute("status");

          // get mutant information
          String mutatedClass = element.getElementsByTagName("mutatedClass").item(0).getTextContent();
          String mutatedMethod = element.getElementsByTagName("mutatedMethod").item(0).getTextContent();
          String lineNumber = element.getElementsByTagName("lineNumber").item(0).getTextContent();
          String succeedingTests = element.getElementsByTagName("succeedingTests").item(0).getTextContent();
          
          // print mutant information
          System.out.println("status :" + status);
          System.out.println("mutatedClass :" + mutatedClass);
          System.out.println("mutatedMethod :" + mutatedMethod);
          System.out.println("lineNumber :" + lineNumber);
          System.out.println("succeedingTests :" + succeedingTests);
          System.out.println();
        }
      }
    } catch(ParserConfigurationException e) {
      e.printStackTrace();
    } catch(SAXException e) {
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}