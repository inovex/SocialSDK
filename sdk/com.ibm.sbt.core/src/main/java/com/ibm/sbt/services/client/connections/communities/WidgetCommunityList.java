package com.ibm.sbt.services.client.connections.communities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.feedhandler.WidgetCommunityFeedHandler;

/**
 * @author Christian Gosch, inovex GmbH
 *
 */
public class WidgetCommunityList extends CommunityList {

	public WidgetCommunityList(Response requestData, WidgetCommunityFeedHandler feedHandler) {
		super(requestData, feedHandler);
	//	dataHandler = new XmlDataHandler(getData(), ConnectionsConstants.nameSpaceCtx);
	}
	
	@Override
	public WidgetCommunityService getService() {
		return (WidgetCommunityService)super.getService();
	}

	@Override
	public WidgetCommunityFeedHandler getFeedHandler() {
		return (WidgetCommunityFeedHandler)super.getFeedHandler();
	}
	
	@Override
	protected WidgetCommunity getEntity(Object data){
		return (WidgetCommunity)super.getEntity(data);
	}
	
	/**
	 * Elements are of type WidgetCommunity! but return type cannot be of type ArrayList&lt;WidgetCommunity&gt;, supposedly because it is not a direct descendant type and the method is protected
	 * @see com.ibm.sbt.services.client.connections.communities.CommunityList#createEntities()
	 */
	@Override
	protected ArrayList<Community> createEntities() {
		
		XmlDataHandler dataHandler = new XmlDataHandler(getData(), nameSpaceCtx);
		ArrayList<Community> communities = new ArrayList<Community>();
		List<Node> entries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		for (Node node: entries) {
			WidgetCommunity community = getEntity(node);
			communities.add(community);
		}
		return communities;
	}
	
}
