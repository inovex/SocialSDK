/*  (c) Copyright IBM Corp. 2010 - Licensed under the Apache License, Version 2.0 */

CodeMirror.defineMode("xml",function(_1,_2){var _3=_1.indentUnit;var _4=_2.htmlMode?{autoSelfClosers:{"area":true,"base":true,"br":true,"col":true,"command":true,"embed":true,"frame":true,"hr":true,"img":true,"input":true,"keygen":true,"link":true,"meta":true,"param":true,"source":true,"track":true,"wbr":true},implicitlyClosed:{"dd":true,"li":true,"optgroup":true,"option":true,"p":true,"rp":true,"rt":true,"tbody":true,"td":true,"tfoot":true,"th":true,"tr":true},contextGrabbers:{"dd":{"dd":true,"dt":true},"dt":{"dd":true,"dt":true},"li":{"li":true},"option":{"option":true,"optgroup":true},"optgroup":{"optgroup":true},"p":{"address":true,"article":true,"aside":true,"blockquote":true,"dir":true,"div":true,"dl":true,"fieldset":true,"footer":true,"form":true,"h1":true,"h2":true,"h3":true,"h4":true,"h5":true,"h6":true,"header":true,"hgroup":true,"hr":true,"menu":true,"nav":true,"ol":true,"p":true,"pre":true,"section":true,"table":true,"ul":true},"rp":{"rp":true,"rt":true},"rt":{"rp":true,"rt":true},"tbody":{"tbody":true,"tfoot":true},"td":{"td":true,"th":true},"tfoot":{"tbody":true},"th":{"td":true,"th":true},"thead":{"tbody":true,"tfoot":true},"tr":{"tr":true}},doNotIndent:{"pre":true},allowUnquoted:true,allowMissing:true}:{autoSelfClosers:{},implicitlyClosed:{},contextGrabbers:{},doNotIndent:{},allowUnquoted:false,allowMissing:false};var _5=_2.alignCDATA;var _6,_7;function _8(_9,_a){function _b(_c){_a.tokenize=_c;return _c(_9,_a);};var ch=_9.next();if(ch=="<"){if(_9.eat("!")){if(_9.eat("[")){if(_9.match("CDATA[")){return _b(_d("atom","]]>"));}else{return null;}}else{if(_9.match("--")){return _b(_d("comment","-->"));}else{if(_9.match("DOCTYPE",true,true)){_9.eatWhile(/[\w\._\-]/);return _b(_e(1));}else{return null;}}}}else{if(_9.eat("?")){_9.eatWhile(/[\w\._\-]/);_a.tokenize=_d("meta","?>");return "meta";}else{var _f=_9.eat("/");_6="";var c;while((c=_9.eat(/[^\s\u00a0=<>\"\'\/?]/))){_6+=c;}if(!_6){return "error";}_7=_f?"closeTag":"openTag";_a.tokenize=_10;return "tag";}}}else{if(ch=="&"){var ok;if(_9.eat("#")){if(_9.eat("x")){ok=_9.eatWhile(/[a-fA-F\d]/)&&_9.eat(";");}else{ok=_9.eatWhile(/[\d]/)&&_9.eat(";");}}else{ok=_9.eatWhile(/[\w\.\-:]/)&&_9.eat(";");}return ok?"atom":"error";}else{_9.eatWhile(/[^&<]/);return null;}}};function _10(_11,_12){var ch=_11.next();if(ch==">"||(ch=="/"&&_11.eat(">"))){_12.tokenize=_8;_7=ch==">"?"endTag":"selfcloseTag";return "tag";}else{if(ch=="="){_7="equals";return null;}else{if(/[\'\"]/.test(ch)){_12.tokenize=_13(ch);return _12.tokenize(_11,_12);}else{_11.eatWhile(/[^\s\u00a0=<>\"\']/);return "word";}}}};function _13(_14){return function(_15,_16){while(!_15.eol()){if(_15.next()==_14){_16.tokenize=_10;break;}}return "string";};};function _d(_17,_18){return function(_19,_1a){while(!_19.eol()){if(_19.match(_18)){_1a.tokenize=_8;break;}_19.next();}return _17;};};function _e(_1b){return function(_1c,_1d){var ch;while((ch=_1c.next())!=null){if(ch=="<"){_1d.tokenize=_e(_1b+1);return _1d.tokenize(_1c,_1d);}else{if(ch==">"){if(_1b==1){_1d.tokenize=_8;break;}else{_1d.tokenize=_e(_1b-1);return _1d.tokenize(_1c,_1d);}}}}return "meta";};};var _1e,_1f;function _20(){for(var i=arguments.length-1;i>=0;i--){_1e.cc.push(arguments[i]);}};function _21(){_20.apply(null,arguments);return true;};function _22(_23,_24){var _25=_4.doNotIndent.hasOwnProperty(_23)||(_1e.context&&_1e.context.noIndent);_1e.context={prev:_1e.context,tagName:_23,indent:_1e.indented,startOfLine:_24,noIndent:_25};};function _26(){if(_1e.context){_1e.context=_1e.context.prev;}};function _27(_28){if(_28=="openTag"){_1e.tagName=_6;return _21(_29,_2a(_1e.startOfLine));}else{if(_28=="closeTag"){var err=false;if(_1e.context){if(_1e.context.tagName!=_6){if(_4.implicitlyClosed.hasOwnProperty(_1e.context.tagName.toLowerCase())){_26();}err=!_1e.context||_1e.context.tagName!=_6;}}else{err=true;}if(err){_1f="error";}return _21(_2b(err));}}return _21();};function _2a(_2c){return function(_2d){var _2e=_1e.tagName;_1e.tagName=null;if(_2d=="selfcloseTag"||(_2d=="endTag"&&_4.autoSelfClosers.hasOwnProperty(_2e.toLowerCase()))){_2f(_2e.toLowerCase());return _21();}if(_2d=="endTag"){_2f(_2e.toLowerCase());_22(_2e,_2c);return _21();}return _21();};};function _2b(err){return function(_30){if(err){_1f="error";}if(_30=="endTag"){_26();return _21();}_1f="error";return _21(arguments.callee);};};function _2f(_31){var _32;while(true){if(!_1e.context){return;}_32=_1e.context.tagName.toLowerCase();if(!_4.contextGrabbers.hasOwnProperty(_32)||!_4.contextGrabbers[_32].hasOwnProperty(_31)){return;}_26();}};function _29(_33){if(_33=="word"){_1f="attribute";return _21(_34,_29);}if(_33=="endTag"||_33=="selfcloseTag"){return _20();}_1f="error";return _21(_29);};function _34(_35){if(_35=="equals"){return _21(_36,_29);}if(!_4.allowMissing){_1f="error";}else{if(_35=="word"){_1f="attribute";}}return (_35=="endTag"||_35=="selfcloseTag")?_20():_21();};function _36(_37){if(_37=="string"){return _21(_38);}if(_37=="word"&&_4.allowUnquoted){_1f="string";return _21();}_1f="error";return (_37=="endTag"||_37=="selfCloseTag")?_20():_21();};function _38(_39){if(_39=="string"){return _21(_38);}else{return _20();}};return {startState:function(){return {tokenize:_8,cc:[],indented:0,startOfLine:true,tagName:null,context:null};},token:function(_3a,_3b){if(_3a.sol()){_3b.startOfLine=true;_3b.indented=_3a.indentation();}if(_3a.eatSpace()){return null;}_1f=_7=_6=null;var _3c=_3b.tokenize(_3a,_3b);_3b.type=_7;if((_3c||_7)&&_3c!="comment"){_1e=_3b;while(true){var _3d=_3b.cc.pop()||_27;if(_3d(_7||_3c)){break;}}}_3b.startOfLine=false;return _1f||_3c;},indent:function(_3e,_3f,_40){var _41=_3e.context;if((_3e.tokenize!=_10&&_3e.tokenize!=_8)||_41&&_41.noIndent){return _40?_40.match(/^(\s*)/)[0].length:0;}if(_5&&/<!\[CDATA\[/.test(_3f)){return 0;}if(_41&&/^<\//.test(_3f)){_41=_41.prev;}while(_41&&!_41.startOfLine){_41=_41.prev;}if(_41){return _41.indent+_3;}else{return 0;}},electricChars:"/",configuration:_2.htmlMode?"html":"xml"};});CodeMirror.defineMIME("text/xml","xml");CodeMirror.defineMIME("application/xml","xml");if(!CodeMirror.mimeModes.hasOwnProperty("text/html")){CodeMirror.defineMIME("text/html",{name:"xml",htmlMode:true});}