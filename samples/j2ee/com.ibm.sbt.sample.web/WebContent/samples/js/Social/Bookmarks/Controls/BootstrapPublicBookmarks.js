require(["sbt/dom", 
         "sbt/connections/controls/bookmarks/BookmarkGrid"], 
         
 function(dom, BookmarkGrid) {
    
	var domNode = dom.byId("publicBookmarksRow");
    var CustomBookmarkRow = domNode.text || domNode.textContent;
	
    var grid = new BookmarkGrid({
		type: "public",
		hideSorter: true,
		hidePager: true
	});

	grid.renderer.template = CustomBookmarkRow;

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});