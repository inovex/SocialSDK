package com.ibm.sbt.services.client.connections.communities;

import java.util.Collection;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This class implements additional services to manage widgets inside a Connections Community.
 * This code should become part of Community, like management services for Bookmarks, Subcommunities etc.
 * The Community Widget itself is modeled by Widget, WidgetXPath etc.
 * 
 * @author Christian Gosch, inovex GmbH, based on code my Carlos Manias
 *
 */
public class WidgetCommunity extends Community {

	/**
	 * Constructor (THIS IS AN EXTENDED COMMUNITY!)
	 *  
	 * @param communityService
	 * @param communityUuid
	 */
	public WidgetCommunity(WidgetCommunityService communityService, String communityUuid) {
		setService(communityService);
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}
	
	public WidgetCommunity(){}
	
	/**
	 * Constructor (THIS IS AN EXTENDED COMMUNITY!)
	 * 
	 * @param communityUuid
	 */
	public WidgetCommunity(String communityUuid) {
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}
	
	/**
	 * Constructor (THIS IS AN EXTENDED COMMUNITY!)
	 * @param svc
	 * @param handler
	 */
	public WidgetCommunity(WidgetCommunityService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	// Overridden persistence methods 
	
	/**
	 * This method loads the "widget enabled" community 
	 * 
	 * @return
	 * @throws CommunityServiceException
	 */
	
	public WidgetCommunity load() throws CommunityServiceException
    {
		return getService().getCommunity(getCommunityUuid());
    }
	
	/**
	 * This method updates the "widget enabled" community on the server
	 * 
	 * @return
	 * @throws CommunityServiceException
	 */
	public WidgetCommunity save() throws CommunityServiceException{
		if(StringUtil.isEmpty(getCommunityUuid())){
			String id = getService().createCommunity(this);
			return getService().getCommunity(id);
		}else{
			getService().updateCommunity(this);
			return getService().getCommunity(getCommunityUuid());
		}
	}
	
	@Override
	public WidgetCommunityService getService(){
		return (WidgetCommunityService)super.getService();
	}
	
	// Additional service methods for managing subcommunities

	/**
	 * This method gets the subcommunities of a community
	 * 
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of members of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
   	 * @return list of sub-communities
	 * @throws CommunityServiceException
	 */
	public WidgetCommunityList getSubCommunities(Map<String, String> parameters) throws CommunityServiceException {
	   	return getService().getSubCommunities(getCommunityUuid(), parameters );
	}
	
	/**
	 * Create sub community for this community as parent.
	 * <br><b>ATTN:</>Sub communities are not allowed to have sub communities themselves!
	 * @param title
	 * @param tags
	 * @param content
	 * @param type
	 * @return
	 */
	public WidgetCommunity createSubCommunity(String title, Collection<String> tags, String content, String type) {
		return getService().createSubCommunity(this, title, tags, content, type);
	}
	
	// Additional service methods for managing Widgets inside a Community
	
	/**  Create widget of given type at this community
	 * @param widgetDefId (required)
	 * @return Widget created
	 */
	public Widget createCommunityWidget(WidgetDefId widgetDefId) throws CommunityServiceException {
		return getService().createCommunityWidget(this, widgetDefId);
	}
	
	/**
	 * This method gets the widgets of a community
	 * 
	 * @return list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets() throws CommunityServiceException {
	   	return getService().getCommunityWidgets(getCommunityUuid());
	}

	/**
	 * This method gets the widgets of a community
	 * 
	 * @param widgetDefId
	 * @return list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(WidgetDefId widgetDefId) throws CommunityServiceException {
	   	return getService().getCommunityWidgets(getCommunityUuid(), widgetDefId);
	}

	/**
	 * This method gets the widgets of a community
	 * 
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of widgets of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
   	 * @return list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(Map<String, String> parameters) throws CommunityServiceException {
	   	return getService().getCommunityWidgets(getCommunityUuid(), parameters );
	}

	/**
	 * This method gets the widgets of a community
	 * 
	 * @param widgetDefId
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of widgets of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
   	 * @return list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(WidgetDefId widgetDefId, Map<String, String> parameters) throws CommunityServiceException {
	   	return getService().getCommunityWidgets(getCommunityUuid(), widgetDefId, parameters );
	}

}
