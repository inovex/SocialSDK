/*  (c) Copyright IBM Corp. 2010 - Licensed under the Apache License, Version 2.0 */

(function(){CodeMirror.defineOption("autoCloseTags",false,function(cm,_1,_2){if(_1&&(_2==CodeMirror.Init||!_2)){var _3={name:"autoCloseTags"};if(typeof _1!="object"||_1.whenClosing){_3["'/'"]=function(cm){_4(cm,"/");};}if(typeof _1!="object"||_1.whenOpening){_3["'>'"]=function(cm){_4(cm,">");};}cm.addKeyMap(_3);}else{if(!_1&&(_2!=CodeMirror.Init&&_2)){cm.removeKeyMap("autoCloseTags");}}});var _5=["area","base","br","col","command","embed","hr","img","input","keygen","link","meta","param","source","track","wbr"];var _6=["applet","blockquote","body","button","div","dl","fieldset","form","frameset","h1","h2","h3","h4","h5","h6","head","html","iframe","layer","legend","object","ol","p","select","table","ul"];function _4(cm,ch){var _7=cm.getCursor(),_8=cm.getTokenAt(_7);var _9=CodeMirror.innerMode(cm.getMode(),_8.state),_a=_9.state;if(_9.mode.name!="xml"){throw CodeMirror.Pass;}var _b=cm.getOption("autoCloseTags"),_c=_9.mode.configuration=="html";var _d=(typeof _b=="object"&&_b.dontCloseTags)||(_c&&_5);var _e=(typeof _b=="object"&&_b.indentTags)||(_c&&_6);if(ch==">"&&_a.tagName){var _f=_a.tagName;if(_8.end>_7.ch){_f=_f.slice(0,_f.length-_8.end+_7.ch);}var _10=_f.toLowerCase();if(_8.type=="tag"&&_a.type=="closeTag"||/\/\s*$/.test(_8.string)||_d&&_11(_d,_10)>-1){throw CodeMirror.Pass;}var _12=_e&&_11(_e,_10)>-1;cm.replaceSelection(">"+(_12?"\n\n":"")+"</"+_f+">",_12?{line:_7.line+1,ch:0}:{line:_7.line,ch:_7.ch+1});if(_12){cm.indentLine(_7.line+1);cm.indentLine(_7.line+2);}return;}else{if(ch=="/"&&_8.type=="tag"&&_8.string=="<"){var _f=_a.context&&_a.context.tagName;if(_f){cm.replaceSelection("/"+_f+">","end");}return;}}throw CodeMirror.Pass;};function _11(_13,elt){if(_13.indexOf){return _13.indexOf(elt);}for(var i=0,e=_13.length;i<e;++i){if(_13[i]==elt){return i;}}return -1;};})();