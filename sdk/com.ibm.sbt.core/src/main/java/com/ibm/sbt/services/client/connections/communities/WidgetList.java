/*
 * Copyright inovex GmbH 2014
 */
package com.ibm.sbt.services.client.connections.communities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.feedhandler.WidgetFeedHandler;

/**
 * 
 * @author Christian Gosch, based on code by Swati Singh
 *
 */
public class WidgetList extends EntityList<Widget> {

	public WidgetList(Response requestData, WidgetFeedHandler widgetFeedHandler) {
		super(requestData, widgetFeedHandler);
	}
	
	@Override
	public Document getData(){
		return (Document)super.getData();
	}
	
	@Override
	public WidgetCommunityService getService() {
		return (WidgetCommunityService)super.getService();
	}

	@Override
	public WidgetFeedHandler getFeedHandler() {
		return (WidgetFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected Widget getEntity(Object data){
		return (Widget)super.getEntity(data);
	}
	
	@Override
	protected ArrayList<Widget> createEntities() {
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), nameSpaceCtx);
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			Widget widget = getEntity(node);
			widgets.add(widget);
		}
		return widgets;
	}
	
	private XmlDataHandler getMetaDataHandler(){
		return new XmlDataHandler(getData(), nameSpaceCtx);
	}

	@Override
	public int getTotalResults() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.TotalResults);
	}

	@Override
	public int getStartIndex() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.StartIndex);
	}

	@Override
	public int getItemsPerPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.ItemsPerPage);
	}

	@Override
	public int getCurrentPage() {
		return getMetaDataHandler().getAsInt(ConnectionsFeedXpath.CurrentPage);
	}
}
