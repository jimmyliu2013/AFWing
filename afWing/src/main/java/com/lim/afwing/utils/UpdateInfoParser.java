package com.lim.afwing.utils;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class UpdateInfoParser
{
    public static UpdateInfo getUpdateInfo(InputStream inStream) throws Exception
    {
        HashMap<String, String> hashMap = new HashMap<String, String>();
		UpdateInfo updateInfo = new UpdateInfo();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++)
        {
            Node childNode = (Node) childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element childElement = (Element) childNode;
                if ("version".equals(childElement.getNodeName()))
                {
					updateInfo.setVersion(childElement.getFirstChild().getNodeValue());
                }
                else if (("description".equals(childElement.getNodeName())))
                {
					updateInfo.setDescription(childElement.getFirstChild().getNodeValue());
                }
                else if (("url".equals(childElement.getNodeName())))
                {
					updateInfo.setUrl(childElement.getFirstChild().getNodeValue());
                }
            }
        }
        return updateInfo;
    }
}