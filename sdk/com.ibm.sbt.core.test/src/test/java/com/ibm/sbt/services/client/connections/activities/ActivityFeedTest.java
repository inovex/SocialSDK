/*
 * � Copyright IBM Corp. 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.connections.activities;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Tag;

/**
 * @author mwallace
 *
 */
public class ActivityFeedTest extends BaseActivityServiceTest {

	@Test
	public void testGetMyActivities() throws ClientServicesException {
		EntityList<Activity> activities = activityService.getMyActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		for (Activity activity : activities) {
			Assert.assertNotNull("Invalid activity id", activity.getId());
		}
	}

	@Test
	public void testGetCompletedActivities() throws ClientServicesException {
		Activity activity = this.activity;
		activity.setCompleted(true);
		activityService.updateActivity(activity);
		
		EntityList<Activity> activities = activityService.getCompletedActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		for (Activity nextActivity : activities) {
			Assert.assertNotNull("Invalid activity id", nextActivity.getId());
		}
	}

	@Test
	public void testGetToDoActivities() throws ClientServicesException {
		Activity todo = createActivity();
		EntityList<Activity> activities = activityService.getToDoActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		for (Activity activity : activities) {
			Assert.assertNotNull("Invalid activity id", activity.getId());
		}
	}

	@Test
	public void testGetActivityTags() throws ClientServicesException {
		EntityList<Tag> tags = activityService.getActivityTags();
		Assert.assertNotNull("Expected non null tags", tags);
		Assert.assertFalse("Expected non empty tags", tags.isEmpty());
		for (Tag tag : tags) {
			Assert.assertNotNull("Invalid tag term", tag.getTerm());
		}
	}
	
}
