/*
 * © Copyright IBM Corp. 2013
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
 
dojo.provide("sbt.connections.controls.profiles.ColleagueGridRenderer");

/**
 * 
 */
define(["../../../declare", "../../../stringUtil",
        "../../../dom","../../../lang",
        "../ConnectionsGridRenderer",
        "../../../i18n!./nls/ColleagueGridRenderer",
        "../../../text!./templates/ColleagueItem.html",
        "../../../text!./templates/ViewAll.html",
        "../../../text!./templates/ColleagueItemFull.html"], 
        function(declare, stringUtil, dom, lang, ConnectionsGridRenderer, nls, colleagueItemTemplate, viewAllTemplate, colleagueItemFullTemplate) {
                
    /**
     * @class ColleagueGridRenderer
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ColleagueGridRenderer
     */
    var ColleagueGridRenderer = declare(ConnectionsGridRenderer, {

         _nls: nls,
         
         containerClass: "lotusChunk",
         
         template: colleagueItemTemplate,
         viewAllTemplate: viewAllTemplate,
         fullTemplate: colleagueItemFullTemplate,
         
         countAlign: "left",
         verticalAlign: "middle",
        
         render: function(grid, el, items, data) {
             while (el.childNodes[0]) {
                 dom.destroy(el.childNodes[0]);
             }
             var size = items.length;
             if (size === 0) {
                this.renderEmpty(grid, el);
             }
             else {
                var container = this.renderContainer(grid, el, items, data);
                for (var i=0; i<items.length; i++) {
                    this.renderItem(grid, container, data, items[i], i, items);
                }
                this.renderViewAll(grid, el, items, data);
             }
          },

          renderContainer: function(grid, el, items, data) {          
              return dom.create("div", { "class": this.containerClass }, el);
          },
          
          renderViewAll: function(grid, el, items, data) {          
              if (this.viewAllTemplate && !grid.hideViewAll) {
                  var node;
                  if (lang.isString(this.viewAllTemplate)) {
                      var domStr = this._substituteItems(this.viewAllTemplate, grid, this, items, data);
                      node = dom.toDom(domStr, el.ownerDocument);
                  } else {
                      node = this.sortTemplate.cloneNode(true);
                  }
                  el.appendChild(node);
                  
                  grid._doAttachEvents(el, data);
              }
          },
          
          viewAllTitle: function(grid, renderer, items, data) {
              var str = (data.totalCount == 1) ? nls.person : nls.people;
              var totalCount = stringUtil.replace(str, { totalCount : data.totalCount });
              return stringUtil.replace(nls.viewAll, { totalCount : totalCount });
          }
    });   
    return ColleagueGridRenderer;
});