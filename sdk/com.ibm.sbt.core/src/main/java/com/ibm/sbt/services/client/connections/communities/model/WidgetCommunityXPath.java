/**
 * 
 */
package com.ibm.sbt.services.client.connections.communities.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * @author hej2sh
 *
 */
public enum WidgetCommunityXPath implements FieldEntry {
	entry("/a:entry"),
	id("./a:id"),
	communityUuid("./snx:communityUuid"), 
	title("./a:title"),
	summary("./a:summary[@type='text']"),
	logoUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href"),
	membersUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href"),
	communityUrl("./a:link[@rel='alternate']/@href"),
	communityAtomUrl("a:link[@rel='self']/@href"),
	tags("./a:category[not(@scheme)]/@term"), 
	content("./a:content[@type='html']"),
	memberCount("./snx:membercount"),
	communityType("./snx:communityType"), 
	published("./a:published"),
	updated("./a:updated"),
	authorUserid("./a:author/snx:userid"),
	authorName("./a:author/a:name"),
	authorEmail("./a:author/a:email"),
	contributorUserid("./a:contributor/snx:userid"),
	contributorName("./a:contributor/a:name"),
	contributorEmail("./a:contributor/a:email"),
	communityTheme("./snx:communityTheme"),
	role("./snx:role"),
	inviteCommunityUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/community']/@href"),
	inviteUrl("./a:link[@rel='edit']/@href"),
	parentCommunityUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/parentcommunity']/@href");
	
	private final XPathExpression path;
	
	private WidgetCommunityXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	@Override
	public XPathExpression getPath() {
		return path;
	}
	
	@Override
	public String getName() {
		return this.name();
	}
}
