/*
 * Copyright inovex GmbH 2014
 */
package com.ibm.sbt.services.client.connections.communities.feedhandler;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.WidgetCommunity;
import com.ibm.sbt.services.client.connections.communities.WidgetCommunityList;
import com.ibm.sbt.services.client.connections.communities.WidgetCommunityService;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * @author Christian Gosch
 *
 */
public class WidgetCommunityFeedHandler extends CommunityFeedHandler {

	private final WidgetCommunityService widgetservice;

	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public WidgetCommunityFeedHandler(WidgetCommunityService service){
		super(service); // required to fulfill Java spec, but super.service is not visible here?????
		this.widgetservice = service; // to prevent FINAL conflict, we have our own service instance here.
	}
	
	/**
	 * @see com.ibm.sbt.services.client.connections.communities.feedhandler.CommunityFeedHandler#createEntity(com.ibm.sbt.services.client.Response)
	 */
	@Override
	public WidgetCommunity createEntity(Response requestData) {
		Node data = (Node)requestData.getData();
		return createEntityFromData(data);
	}

	/**
	 * @see com.ibm.sbt.services.client.connections.communities.feedhandler.CommunityFeedHandler#createEntityFromData(java.lang.Object)
	 */
	@Override
	public WidgetCommunity createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)CommunityXPath.entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, nameSpaceCtx, expr);
		WidgetCommunity community = new WidgetCommunity(widgetservice, handler);
		return community;
	}

	/**
	 * @see com.ibm.sbt.services.client.connections.communities.feedhandler.CommunityFeedHandler#createEntityList(com.ibm.sbt.services.client.Response)
	 */
	@Override
	public WidgetCommunityList createEntityList(Response requestData) {
		return new WidgetCommunityList((Response)requestData, this);
	}

	/**
	 * @see com.ibm.sbt.services.client.connections.communities.feedhandler.CommunityFeedHandler#getService()
	 */
	@Override
	public WidgetCommunityService getService() {
		return widgetservice;
	}
	
}
