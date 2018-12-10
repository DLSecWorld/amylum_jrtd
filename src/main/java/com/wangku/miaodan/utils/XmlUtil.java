package com.wangku.miaodan.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * 创建时间：2016年11月3日 上午11:40:17
 * 
 * @author andy
 * @version 2.2
 */

public class XmlUtil {

	private static final String PREFIX_XML = "<xml>";

	private static final String SUFFIX_XML = "</xml>";

	private static final String PREFIX_CDATA = "<![CDATA[";

	private static final String SUFFIX_CDATA = "]]>";

	/**
	 * 转化成xml, 单层无嵌套
	 * 
	 * @param map
	 * @param isAddCDATA
	 * @return
	 */
	public static String xmlFormat(Map<String, String> parm, boolean isAddCDATA) {

		StringBuffer strbuff = new StringBuffer(PREFIX_XML);
		if (CollectionUtil.isNotEmpty(parm)) {
			for (Entry<String, String> entry : parm.entrySet()) {
				strbuff.append("<").append(entry.getKey()).append(">");
				if (isAddCDATA) {
					strbuff.append(PREFIX_CDATA);
					if (StringUtil.isNotEmpty(entry.getValue())) {
						strbuff.append(entry.getValue());
					}
					strbuff.append(SUFFIX_CDATA);
				} else {
					if (StringUtil.isNotEmpty(entry.getValue())) {
						strbuff.append(entry.getValue());
					}
				}
				strbuff.append("</").append(entry.getKey()).append(">");
			}
		}
		return strbuff.append(SUFFIX_XML).toString();
	}

	/**
	 * 解析xml
	 * 
	 * @param xml
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static Map<String, String> xmlParse(String xml) {
		Map<String, String> map = null;
		if (StringUtil.isNotEmpty(xml)) {
			SAXReader reader = new SAXReader();
			try {
				xml = xml.replace("<![CDATA[", "").replace("]]>", "");
				Document doc = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
				Element root = doc.getRootElement();
				if (root.hasContent()) {
					map = new HashMap<String, String>();
					List<Element> elements = root.elements();
					for (Element element : elements) {
						map.put(element.getName(), element.getTextTrim());
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}	
}
