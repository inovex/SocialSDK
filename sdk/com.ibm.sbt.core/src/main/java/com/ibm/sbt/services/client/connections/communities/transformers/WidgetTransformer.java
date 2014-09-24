package com.ibm.sbt.services.client.connections.communities.transformers;

import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.communities.Widget;
import com.ibm.sbt.services.client.connections.communities.model.WidgetXPath;
import com.ibm.sbt.services.util.XmlTextUtil;

/**
 * @author Christian Gosch, inovex GmbH
 *
 */
public class WidgetTransformer extends AbstractBaseTransformer {
	
	private Widget widget;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/communities/templates/";
	
	/*
	 * Tranformer needs instance of widget 
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	public WidgetTransformer(Widget widget) {
		this.widget = widget;
	}

	/** 
	 * @see com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer#transform(java.util.Map)
	 */
	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {
		
		String xml = getTemplateContent(sourcepath+"WidgetTmpl.xml");
		
		String titleXml = "";
		String widgetDefIdXml = "";
		String widgetInstanceIdXml = "";
		String hiddenXml = "";
		String locationXml = "";
		String previousWidgetInstanceIdXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			
			if(currentElement.equalsIgnoreCase(WidgetXPath.title.toString())){
				titleXml = getXMLRep(getStream(sourcepath+"CommunityTitleTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			} else if (currentElement.equalsIgnoreCase(WidgetXPath.widgetDefId.toString())) {
				widgetDefIdXml = getXMLRep(getStream(sourcepath+"WidgetDefIdTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			} else if (currentElement.equalsIgnoreCase(WidgetXPath.widgetInstanceId.toString())) {
				widgetInstanceIdXml = getXMLRep(getStream(sourcepath+"WidgetInstIdTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			} else if (currentElement.equalsIgnoreCase(WidgetXPath.widgetHidden.toString())) {
				hiddenXml = getXMLRep(getStream(sourcepath+"WidgetHidden.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			} else if (currentElement.equalsIgnoreCase(WidgetXPath.widgetLocation.toString())) {
				locationXml = getXMLRep(getStream(sourcepath+"WidgetLocation.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			} else if (currentElement.equalsIgnoreCase(WidgetXPath.previousWidgetInstanceId.toString())) {
				previousWidgetInstanceIdXml = getXMLRep(getStream(sourcepath+"WidgetPrevIdTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(widgetDefIdXml)){
			xml = getXMLRep(xml, "getWidgetDefId",widgetDefIdXml);
		}
		if(StringUtil.isNotEmpty(widgetInstanceIdXml)){
			xml = getXMLRep(xml, "getWidgetInstanceId",widgetInstanceIdXml);
		}
		if(StringUtil.isNotEmpty(hiddenXml)){
			xml = getXMLRep(xml, "getHidden",hiddenXml);
		} else {
			
		}
		if(StringUtil.isNotEmpty(locationXml)){
			xml = getXMLRep(xml, "getLocation",locationXml);
		}
		if(StringUtil.isNotEmpty(previousWidgetInstanceIdXml)){
			xml = getXMLRep(xml, "getPreviousWidgetInstanceId",previousWidgetInstanceIdXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}

}
