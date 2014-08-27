/**
 * 
 */
package com.ibm.sbt.services.client.connections.communities.transformers;

import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.communities.WidgetCommunity;
import com.ibm.sbt.services.client.connections.communities.model.WidgetCommunityXPath;
import com.ibm.sbt.services.util.XmlTextUtil;

/**
 * @author hej2sh
 *
 */
public class WidgetCommunityTransformer extends CommunityTransformer {

	private WidgetCommunity community;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/communities/templates/";
	
	/*
	 * Tranformer needs instance of community 
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	public WidgetCommunityTransformer(WidgetCommunity community) {
		super(community);
		this.community = community;
	}
	
	@Override
	public String transform(Map<String,Object> fieldmap) throws TransformerException{
		
		String xml = getTemplateContent(sourcepath+"WidgetCommunityTmpl.xml");
		String tagsXml = "";
		String contentXml = "";
		String titleXml = "";
		String typeXml = "";
		String communityUuidXml = "";
		String communityIdXml = "";
		String parentCommunityUrlXml = "";
		
		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains(WidgetCommunityXPath.tags.toString())){
				tagsXml += getXMLRep(getStream(sourcepath+"CategoryTmpl.xml"),"tag",XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(WidgetCommunityXPath.content.toString())){
				contentXml = getXMLRep(getStream(sourcepath+"CommunityContentTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(WidgetCommunityXPath.title.toString())){
				titleXml = getXMLRep(getStream(sourcepath+"CommunityTitleTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(WidgetCommunityXPath.communityType.toString())){
				typeXml = getXMLRep(getStream(sourcepath+"CommunityTypeTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(WidgetCommunityXPath.communityUuid.toString())){
				communityUuidXml = getXMLRep(getStream(sourcepath+"CommunityUuidTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
				communityIdXml = getXMLRep(getStream(sourcepath+"CommunityIdTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}else if(currentElement.equalsIgnoreCase(WidgetCommunityXPath.parentCommunityUrl.toString())){
				parentCommunityUrlXml = getXMLRep(getStream(sourcepath+"WidgetCommunityParentTmpl.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		else{ // need to provide empty content in payload, in case it is not set
			contentXml = getXMLRep(getStream(sourcepath+"CommunityContentTemplate.xml"),"content","");
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		if(StringUtil.isNotEmpty(tagsXml)){
			xml = getXMLRep(xml, "getTags",tagsXml);
		}
		if(StringUtil.isNotEmpty(communityUuidXml)){
			xml = getXMLRep(xml, "getCommunityUuid",communityUuidXml);
		}
		if(StringUtil.isNotEmpty(communityIdXml)){
			xml = getXMLRep(xml, "getCommunityId",communityIdXml);
		}
		if(StringUtil.isNotEmpty(parentCommunityUrlXml)){
			xml = getXMLRep(xml, "getParentCommunityUrlXml",parentCommunityUrlXml);
		}
		
		if(StringUtil.isNotEmpty(typeXml)){
			xml = getXMLRep(xml, "getCommunityType",typeXml);
		}else{
			String commType = "";
			try {
				commType = community.getCommunityType();
			} catch (Exception e) {
			}
			if(StringUtil.isEmpty(commType)){ // must be for creating a new community, default to public
				typeXml = getXMLRep(getStream(sourcepath+"CommunityTypeTemplate.xml"),"communityType","public");
			}else{ // for update scenario
				typeXml = getXMLRep(getStream(sourcepath+"CommunityTypeTemplate.xml"),"communityType",community.getCommunityType());
			}
			xml = getXMLRep(xml, "getCommunityType",typeXml);
		}
		
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}
