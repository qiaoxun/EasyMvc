package com.test.util.bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.test.util.StringUtils;

public class EasyMvcXmlUtils extends DefaultHandler {
	
	private Map<String, String> dataMap = new HashMap<>();
	
	/**
	 * 初始化，获取数据
	 * @param xmlFilePath
	 */
	public static Map<String, String> init(String xmlFilePath) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			
			EasyMvcXmlUtils utils = new EasyMvcXmlUtils();
			
			saxParser.parse(new FileInputStream(xmlFilePath), utils);
			
			return utils.getDataMap();
			
		} catch (ParserConfigurationException | IOException | SAXException e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (StringUtils.equals(qName, "scan")) {
			dataMap.put(Constant.baseScanPackage, attributes.getValue("base-package"));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	public Map<String, String> getDataMap() {
		return dataMap;
	}
	
}
