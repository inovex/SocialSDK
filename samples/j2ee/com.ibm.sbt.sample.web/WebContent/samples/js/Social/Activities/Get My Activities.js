require(["sbt/connections/ActivitiesService", "sbt/dom"], 
    function(ActivitiesService,dom) {
        var createRow = function(title, activityId) {
            var table = dom.byId("communitiesTable");
            var tr = document.createElement("tr");
            table.appendChild(tr);
            var td = document.createElement("td");
            td.innerHTML = title;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = activityId;
            tr.appendChild(td);
        };

        var activitiesService = new ActivitiesService();
        activitiesService.getMyActivities().then(
            function(activities) {
                if (activities.length == 0) {
                    text = "You do not have any activities.";
                } else {
                    for(var i=0; i<activities.length; i++){
                        var activitiy = activities[i];
                        var title = activitiy.getTitle(); 
                        var activityId = activitiy.getActivityId(); 
                        createRow(title, activityId);
                    }
                }
            },
            function(error) {
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);