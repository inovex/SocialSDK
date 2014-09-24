package com.ibm.sbt.services.client.connections.communities.feedhandler;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.Widget;
import com.ibm.sbt.services.client.connections.communities.WidgetCommunityService;
import com.ibm.sbt.services.client.connections.communities.WidgetList;
import com.ibm.sbt.services.client.connections.communities.model.WidgetXPath;

/**
 * 
 * @author Christian Gosch, inovex GmbH, based on code by Swati Singh
 *
 */
public class WidgetFeedHandler implements IFeedHandler {

	private final WidgetCommunityService service;
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public WidgetFeedHandler(WidgetCommunityService service){
		this.service = service;
	}
	
	/**
	 * @param requestData
	 * @return Widget
	 */
	@Override
	public Widget createEntity(Response requestData) {
		// getData() returns String (supposedly XML?), but not Node.
		// How the heck is that XML converted to a Node?
		// Where is the marshaller magic hidden?
		Node data = (Node)requestData.getData(); 
		return createEntityFromData(data);
	}
	
	/**
	 * @param data object
	 * @return Widget
	 */
	@Override
	public Widget createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)WidgetXPath.entry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, nameSpaceCtx, expr);
		Widget widget = new Widget(service, handler);
		return widget;
	}

	/**
	 * @param data object
	 * @return Collection of communities
	 */
	@Override
	public WidgetList createEntityList(Response requestData) {
		return new WidgetList((Response)requestData, this);
	}

	/**
	 * @return WidgetCommunityService
	 */
	@Override
	public WidgetCommunityService getService() {
		return service;
	}

}
