/*
 * Copyright inovex GmbH 2014
 */
package com.ibm.sbt.services.client.connections.communities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.communities.feedhandler.WidgetCommunityFeedHandler;
import com.ibm.sbt.services.client.connections.communities.feedhandler.WidgetFeedHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.connections.communities.util.Messages;


/**
 * This class implements additional services to manage widgets inside a Connections Community.
 * This code should become part of CommunityService, like management services for Bookmarks, Subcommunities etc.
 * The Community Widget itself is modeled by Widget, WidgetXPath etc.
 * 
 * @author Christian Gosch, based on code my Carlos Manias
 */
@SuppressWarnings("serial")
public class WidgetCommunityService extends CommunityService {
	
	/** Class logger */
	private static final Logger LOG = Logger.getLogger(WidgetCommunityService.class.toString());
	
	private static final String className = "WidgetCommunityService";
	
	private static final String COMMUNITY_UNIQUE_IDENTIFIER = "communityUuid";
	
	public static final String COMMUNITYTYPE_PUBLIC = "public";
	public static final String COMMUNITYTYPE_RESTRICTED = "publicInviteOnly";
	public static final String COMMUNITYTYPE_PRIVATE = "private";
	
	public static final String	WidgetsException					= "Problem occurred while fetching Widgets of Community with id : {0}";
	public static final String	WidgetDefIdException				= "Missing Widget type definition";
	
	public static final String WIDGET_DEF_ID = "widgetDefId";

	/**
	 * Wrapper method to get a "Widget enabled" Community
	 * <p>
	 * fetches community content from server and populates the data member of {@link Community} with the fetched content 
	 * @see CommunityService#getCommunity(String)
	 *
	 * @param communityUuid
	 *			   id of community
	 * @return A Community
	 * @throws CommunityServiceException
	 */
	@Override
	public WidgetCommunity getCommunity(String communityUuid) throws CommunityServiceException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
        String url = CommunityUrls.COMMUNITY_INSTANCE.format(this);
		WidgetCommunity community;
		try {
			community = (WidgetCommunity)getEntity(url, parameters, new WidgetCommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CommunityException, communityUuid);
		} catch (Exception e) {
			throw new CommunityServiceException(e, Messages.CommunityException, communityUuid);
		}
		
		return community;
	}
	
	/**
	 * Wrapper method to get Communities of which the user is a member or owner.
	 * 
	 * @return A list of communities of which the user is a member or owner
	 * @throws CommunityServiceException
	 */
	@Override
	public WidgetCommunityList getMyCommunities(Map<String, String> parameters) throws CommunityServiceException {
        String requestUrl = CommunityUrls.COMMUNITIES_MY.format(this);
			
		WidgetCommunityList communities = null;
		if(null == parameters){
			 parameters = new HashMap<String, String>();
		}
		try {
			communities = (WidgetCommunityList) getEntities(requestUrl, parameters, new WidgetCommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.MyCommunitiesException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.MyCommunitiesException);
		}
		
		return communities;
	}
	
	/**
	 * Create sub community for given parent community.
	 * <br><b>ATTN:</>Sub communities are not allowed to have sub communities themselves!
	 * @param parentCommunity
	 * @param title
	 * @param tags
	 * @param content
	 * @param type
	 * @return
	 */
	public WidgetCommunity createSubCommunity(Community parentCommunity, String title, Collection<String> tags, String content, String type) {
		final String sMethod = className + ".createSubCommunity(): ";
		
		WidgetCommunity subCommunity = null;
		try {
			// set up new community object
			WidgetCommunity community = prepareCommunityEntity(title, tags, content, type, null, null);
			
			// create sub community
			
			// stripped together from CommunityService.createCommunity(community) and ForumService.createCommunityTopic(topic, communityID).
			// Currently, the SBT SDK does not contain useful code to create entities inside a community :-(
			// assumption: the "sub communities URL" must be used for POST and PUT as well. (PUT with "instance community" URL breaks sub community relation!)
			// this makes it impossible to have the sub communities UUID in the URL. There is only the parent community UUID
			// but the sub community UUID is part of the payload!
			
			Object communityPayload =  community.constructCreateRequestBody();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put(COMMUNITY_UNIQUE_IDENTIFIER, parentCommunity.getCommunityUuid()); // CommunityService.COMMUNITY_UNIQUE_IDENTIFIER

			Map<String, String> requestheaders = new HashMap<String, String>();
			requestheaders.put("Content-Type", "application/atom+xml");

			StringBuilder comBaseUrl = new StringBuilder(CommunityUrls.COMMUNITY_SUBCOMMUNITIES.format(this)); // "{communities}/service/atom/{authType}/community/subcommunities"
	
			// Add required parameters
			if (null != params && params.size() > 0) {
				comBaseUrl.append('?');
				boolean setSeparator = false;
				for (Map.Entry<String, String> param : params.entrySet()) {
					String key = param.getKey();
					if (StringUtil.isEmpty(key)) continue;
					String value = EntityUtil.encodeURLParam(param.getValue());
					if (StringUtil.isEmpty(value)) continue;
					if (setSeparator) {
						comBaseUrl.append('&');
					} else {
						setSeparator = true;
					}
					comBaseUrl.append(key).append('=').append(value);
				}
			}
	
			String communityUrl = comBaseUrl.toString();
			LOG.log(Level.FINE, sMethod + "communityUrl: " + communityUrl);
			
			Response requestData = this.createData(communityUrl, null, requestheaders, communityPayload, ClientService.FORMAT_CONNECTIONS_OUTPUT);
			
			// community.clearFieldsMap();
			
			// uuid = svc.extractCommunityIdFromHeaders(requestData);
			Header[] headers = requestData.getResponse().getAllHeaders();
			String urlLocation = "";
			for (Header header: headers){
				if (header.getName().equalsIgnoreCase("Location")) {
					urlLocation = header.getValue();
				}
			}
			String uuid = urlLocation.substring(urlLocation.indexOf(COMMUNITY_UNIQUE_IDENTIFIER+"=") + (COMMUNITY_UNIQUE_IDENTIFIER+"=").length());
			// END OF svc.extractCommunityIdFromHeaders(requestData);
			
			// load community object immediately after creation to reflect automatic extensions
			subCommunity = this.getCommunity(uuid);
			if (null == subCommunity) {
				LOG.log(Level.FINE, sMethod + "community NOT created: " + title);
				LOG.log(Level.FINE, sMethod + "community NOT created, tag: " + tags);
			} else {
				LOG.log(Level.FINE, sMethod + "community created: " + subCommunity.getTitle());
				LOG.log(Level.FINE, sMethod + "community created, tags: " + subCommunity.getTags());
			}
		} catch (Throwable t) {
			// log exception
			LOG.log(Level.WARNING, sMethod + "Exception thrown: " + t);
			LOG.log(Level.WARNING, sMethod + "Caused by: " + t.getCause());
			LOG.log(Level.WARNING, sMethod + "", t);
			LOG.log(Level.WARNING, sMethod + "~~~~~~~~~~~~~~~~~~~~~~");
		}
		return subCommunity;
	}
	
	/**
	 * Update sub community for given parent community.
	 * <br><b>ATTN:</>Sub communities are not allowed to have sub communities themselves!
	 * @param parentCommunity
	 * @param subCommunity
	 * @return
	 */
	public WidgetCommunity updateSubCommunity(Community parentCommunity, Community subCommunity) {
		return updateSubCommunity(parentCommunity, subCommunity.getTitle(), subCommunity.getTags(), subCommunity.getContent(), subCommunity.getCommunityType(), subCommunity.getCommunityUuid());
	}
	
	/**
	 * Update sub community for given parent community.
	 * <br><b>ATTN:</>Sub communities are not allowed to have sub communities themselves!
	 * @param parentCommunity
	 * @param title
	 * @param tags
	 * @param content
	 * @param type
	 * @param communityUuid
	 * @return
	 */
	public WidgetCommunity updateSubCommunity(Community parentCommunity, String title, Collection<String> tags, String content, String type, String communityUuid) {
		final String sMethod = className + ".updateSubCommunity(): ";
		
		WidgetCommunity subCommunity = null;
		try {
			// set up new community object
			WidgetCommunity community = prepareCommunityEntity(title, tags, content, type, communityUuid, parentCommunity);
			
			// update sub community
			
			// stripped together from CommunityService.createCommunity(community) and ForumService.createCommunityTopic(topic, communityID).
			// Currently, the SBT SDK does not contain useful code to create entities inside a community :-(
			// assumption: the "sub communities URL" must be used for POST and PUT as well. (PUT with "instance community" URL breaks sub community relation!)
			// this makes it impossible to have the sub communities UUID in the URL. There is only the parent community UUID
			// but the sub community UUID is part of the payload!
			
			Object communityPayload =  community.constructCreateRequestBody();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);

			Map<String, String> requestheaders = new HashMap<String, String>();
			requestheaders.put("Content-Type", "application/atom+xml");

			String communityUrl = WidgetCommunityUrls.COMMUNITY_INSTANCE.format(this); // "{communities}/service/atom/{authType}/community/instance"
			LOG.log(Level.FINE, sMethod + "communityUrl: " + communityUrl);
			
			this.updateData(communityUrl, params,communityPayload, COMMUNITY_UNIQUE_IDENTIFIER);
			
			community.clearFieldsMap();
			
			// load community object immediately after creation to reflect automatic extensions
			subCommunity = this.getCommunity(communityUuid);
			if (null == subCommunity) {
				LOG.log(Level.FINE, sMethod + "community NOT created: " + title);
				LOG.log(Level.FINE, sMethod + "community NOT created, tag: " + tags);
			} else {
				LOG.log(Level.FINE, sMethod + "community created: " + subCommunity.getTitle());
				LOG.log(Level.FINE, sMethod + "community created, tags: " + subCommunity.getTags());
			}
		} catch (Throwable t) {
			// log exception
			LOG.log(Level.WARNING, sMethod + "Exception thrown: " + t);
			LOG.log(Level.WARNING, sMethod + "Caused by: " + t.getCause());
			LOG.log(Level.WARNING, sMethod + "", t);
			LOG.log(Level.WARNING, sMethod + "~~~~~~~~~~~~~~~~~~~~~~");
		}
		return subCommunity;
	}
	
	private WidgetCommunity prepareCommunityEntity(String title, Collection<String> tags, String content, String type, String uuid, Community parentCommunity) {
		final String sMethod = className + ".prepareCommunityEntity(): ";
		// set up new community object
		WidgetCommunity community = new WidgetCommunity();
		if (null != uuid) {
			// for update only: "id" is required!
			community.setCommunityUuid(uuid);
			community.setAsString(CommunityXPath.communityUuid, uuid);
			community.setAsString(CommunityXPath.id, uuid);
		}
		if (null != parentCommunity && null != parentCommunity.getCommunityUuid()) {
			String communityUrl = (parentCommunity.getCommunityUrl());
			LOG.log(Level.FINE, sMethod + "parent community VIEW URL: " + communityUrl);
			// BAD:  https://fe0vmc0449.de.bosch.com/communities/service/html/communityview?communityUuid=8ea4ff45-ef58-4c9b-b131-def2d3e233f7
			// GOOD: https://fe0vmc0449.de.bosch.com/communities/service/atom/community/instance?communityUuid=8ea4ff45-ef58-4c9b-b131-def2d3e233f7
			communityUrl = StringUtil.replace(communityUrl, "/communities/service/html/communityview?", "/communities/service/atom/community/instance?");
			LOG.log(Level.FINE, sMethod + "parent community REL  URL: " + communityUrl);
			community.setParentCommunityUrl(communityUrl);
			LOG.log(Level.FINE, sMethod + "reference             URL: https://fe0vmc0449.de.bosch.com/communities/service/atom/community/instance?communityUuid=8ea4ff45-ef58-4c9b-b131-def2d3e233f7");
		}
		community.setTitle(title); // required
		// prepare tags
		HashSet<String> interim = new HashSet<String>();
		for (Iterator<String> iterator = tags.iterator(); iterator.hasNext();) {
			String tag = (String) iterator.next();
			interim.add(tag.toLowerCase());
		}
		tags = interim;
		community.setTags(new ArrayList<String>(tags));
		community.setContent(content);
		// set type: 
			// - public (view:all, edit:all, membership:self/invite) 
			// - private (view:members, edit:members, membership:invite) 
			// - publicInviteOnly (view:all, edit:?, membership:invite)
		if (COMMUNITYTYPE_PUBLIC.equals(type) || COMMUNITYTYPE_PRIVATE.equals(type) || COMMUNITYTYPE_RESTRICTED.equals(type)) {
			community.setCommunityType(type);
		} else {
			community.setCommunityType(COMMUNITYTYPE_PUBLIC);
		}
		
		return community;
	}
	
	/**
	 * Wrapper method to get SubCommunities of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which SubCommunities are to be fetched
	 * @return A list of communities
	 * @throws CommunityServiceException
	 */
	public WidgetCommunityList getSubCommunities(String communityUuid, Map<String, String> parameters) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
	
        String requestUrl = WidgetCommunityUrls.COMMUNITY_SUBCOMMUNITIES.format(this);
		
		WidgetCommunityList communities = null;
		try {
			communities = (WidgetCommunityList) getEntities(requestUrl, parameters, new WidgetCommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.SubCommunitiesException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.SubCommunitiesException, communityUuid);
		}
		
		return communities;
	}
	
	/**  Create widget of given type at given community
	 * @param community (required)
	 * @param widgetDefId (required)
	 * @return Widget created
	 */
	public Widget createCommunityWidget(WidgetCommunity community, WidgetDefId widgetDefId) throws CommunityServiceException {
		final String sMethod = className + ".createCommunityWidget(): ";
		if (null == community) {
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if (null == widgetDefId) {
			throw new CommunityServiceException(null, WidgetDefIdException);
		}
		
		Widget communityWidget = null;
		
		try {
			
			Widget widget = new Widget();
			widget.setWidgetDefId(widgetDefId.toString());
			widget.setWidgetLocation("col2"); // required unless hidden is not true
			widget.setWidgetHidden(Boolean.FALSE); // optional, default: false
			
			Object widgetPayload = widget.constructCreateRequestBody();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put(COMMUNITY_UNIQUE_IDENTIFIER, community.getCommunityUuid()); // CommunityService.COMMUNITY_UNIQUE_IDENTIFIER
//			params.put(WIDGET_DEF_ID, widgetDefId.toString());

			Map<String, String> requestheaders = new HashMap<String, String>();
			requestheaders.put("Content-Type", "application/atom+xml");

			StringBuilder comBaseUrl = new StringBuilder(WidgetCommunityUrls.COMMUNITY_WIDGETS_FEED.format(this)); // "{communities}/service/atom/{authType}/community/widgets"
	
			// Add required parameters
			if (null != params && params.size() > 0) {
				comBaseUrl.append('?');
				boolean setSeparator = false;
				for (Map.Entry<String, String> param : params.entrySet()) {
					String key = param.getKey();
					if (StringUtil.isEmpty(key)) continue;
					String value = EntityUtil.encodeURLParam(param.getValue());
					if (StringUtil.isEmpty(value)) continue;
					if (setSeparator) {
						comBaseUrl.append('&');
					} else {
						setSeparator = true;
					}
					comBaseUrl.append(key).append('=').append(value);
				}
			}
	
			String widgetPostUrl = comBaseUrl.toString();
			LOG.log(Level.FINE, sMethod + "widgetPostUrl: " + widgetPostUrl);
			
			// execute request
			
			Response result = createData(widgetPostUrl, null, requestheaders, widgetPayload);
			
			// read response data
			
			WidgetFeedHandler feedHandler = new WidgetFeedHandler(this);
			widget = feedHandler.createEntity(result);
			
			WidgetList widgetList = getCommunityWidgets(community.getCommunityUuid(), widgetDefId);
			if (null != widgetList && widgetList.size() > 0) {
				communityWidget = widgetList.get(0);
			}
		} catch (Throwable t) {
			// log exception
			LOG.log(Level.WARNING, sMethod + "Exception thrown: " + t);
			LOG.log(Level.WARNING, sMethod + "Caused by: " + t.getCause());
			LOG.log(Level.WARNING, sMethod + "", t);
			LOG.log(Level.WARNING, sMethod + "~~~~~~~~~~~~~~~~~~~~~~");
		}
		
		return communityWidget;
	}

	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @return A list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(String communityUuid) throws CommunityServiceException {
		return getCommunityWidgets(communityUuid,	null, null);
	}
	
	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @param widgetDefId
	 * @return A list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(String communityUuid, WidgetDefId widgetDefId) throws CommunityServiceException {
		return getCommunityWidgets(communityUuid,	widgetDefId, null);
	}
	
	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @param parameters
	 * @return A list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(String communityUuid, Map<String, String> parameters) throws CommunityServiceException {
		return getCommunityWidgets(communityUuid,	null, parameters);
	}
	
	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @param widgetDefId
	 * @param parameters
	 * @return A list of widgets
	 * @throws CommunityServiceException
	 */
	public WidgetList getCommunityWidgets(String communityUuid, WidgetDefId widgetDefId, Map<String, String> parameters) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if (null != widgetDefId) {
			parameters.put(WIDGET_DEF_ID, widgetDefId.toString());
		}
	
        String requestUrl = WidgetCommunityUrls.COMMUNITY_WIDGETS_FEED.format(this);
		
		WidgetList widgets = null;
		try {
			widgets = (WidgetList) getEntities(requestUrl, parameters, new WidgetFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, WidgetsException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, WidgetsException, communityUuid);
		}
		
		return widgets;
	}

}
