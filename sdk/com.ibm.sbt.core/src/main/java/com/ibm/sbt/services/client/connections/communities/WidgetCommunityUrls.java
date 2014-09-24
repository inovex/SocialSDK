package com.ibm.sbt.services.client.connections.communities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author Christian Gosch, inovex GmbH, based on code by Carlos Manias
 */
public enum WidgetCommunityUrls implements URLContainer {
	COMMUNITIES_ALL(new VersionedUrl(v4_0, 			"{communities}/service/atom/{authType}/communities/all")),
	COMMUNITIES_MY(new VersionedUrl(v4_0, 			"{communities}/service/atom/{authType}/communities/my")),
	COMMUNITY_INSTANCE(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/instance")),
	COMMUNITY_MEMBERS(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/members")),
	COMMUNITY_SUBCOMMUNITIES(new VersionedUrl(v4_0, "{communities}/service/atom/{authType}/community/subcommunities")),
	COMMUNITY_BOOKMARKS(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/bookmarks")),
	COMMUNITY_FORUMTOPICS(new VersionedUrl(v4_0, 	"{communities}/service/atom/{authType}/community/forum/topics")),
	COMMUNITY_MYINVITES(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/invites/my")),
	COMMUNITY_INVITES(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/invites")),
	// CommunityType remoteApplications???

	/** Community widgets Atom Feed URL; parameters: communityUuid; widgetInstanceId */
	COMMUNITY_WIDGETS_FEED(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/widgets")),
	/** Community widgets Browser/HTML URL; parameters: communityUuid; filter=status */
	COMMUNITY_WIDGETS_HTML(new VersionedUrl(v4_0, 		"{communities}/service/html/{authType}/community/updates"));

	private URLBuilder builder;
	
	private WidgetCommunityUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
