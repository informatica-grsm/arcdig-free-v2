<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %><%@ taglib uri="http://www.zkoss.org/dsp/zk/core" prefix="z" %><%@ taglib uri="http://www.zkoss.org/dsp/web/theme" prefix="t" %>body{overflow-x:hidden;margin-top:-9px;padding:0!important}#tooltip{position:absolute;display:none;border:0;padding:3px 8px;border-radius:3px;font-size:10px;background-color:#222;color:#fff;z-index:25}a:hover,a:active,a:visited,a:focus{text-decoration:none;cursor:pointer;color:#333}.header{height:80px;position:relative}.header h1{padding-left:70px;left:15px;overflow:hidden;position:relative;top:20px;width:131px;background:url(${c:encodeURL('../img/top_zk_logo.png')}) no-repeat transparent;opacity:.7}.header h1 a{color:#adadad;text-shadow:0 1px #fff;font-size:40px;display:block;font-weight:normal;line-height:54px}.search{position:absolute;z-index:20;top:6px;left:230px}.user-nav{position:absolute;right:30px;top:5px;z-index:20;margin:0}.sidebar{display:block;float:left;position:relative;width:220px;z-index:16}.content{background:#eee;margin-right:0;padding-bottom:25px;position:relative;min-height:500px;width:auto;-webkit-background-clip:padding-box;border-top-left-radius:8px;z-index:20}.content-header{${t:gradient('ver','#ffffff 0%; #eeeeee 100%') };border-top-left-radius:8px;min-height:2px;padding-top:5px;width:100%;margin-top:0px;z-index:20}.content-header h1{color:#555;font-size:28px;font-weight:normal;text-shadow:0 1px 0 #fff;margin-left:20px;margin-top:20px}.content-header .btn-group{float:right;right:20px;}.breadcrumb{background-color:#e5e5e5;box-shadow:0 0 1px #fff;border-top:1px solid #d6d6d6;border-bottom:1px solid #d6d6d6;padding-left:10px;padding:0}.breadcrumb a{padding:8px 20px 8px 10px;display:inline-block;font-size:11px;color:#666}.breadcrumb a i{opacity:.6;margin-right:6px;vertical-align:text-bottom}.breadcrumb a:hover i{opacity:1}.breadcrumb a:after{content:"\f054";display:inline-block;font-family:FontAwesome;font-weight:normal;font-style:normal;text-decoration:inherit;-webkit-font-smoothing:antialiased;margin:0 -20px 0 13px}.breadcrumb a:last-child:after{display:none}.style-switcher{position:absolute;width:250px;height:30px;background-color:#000;z-index:30;right:0;top:129px;border-radius:5px 0 0 5px;margin-right:-220px}.style-switcher i{display:inline-block;margin:-4px 10px 0 10px;cursor:pointer}.style-switcher span{font-weight:bold;color:#fff;display:inline-block;margin:-15px 20px 0 0;vertical-align:middle}.style-switcher a{display:inline-block;width:20px;height:20px;margin-top:4px;border-style:solid;border-width:1px;border-color:transparent}
.conversation.z-panel .z-caption {
    padding-left: 0px;
}

.conversation.z-panel .z-caption  {
    padding-left: 0px;
} 
.conversation.z-panel .z-caption-content {
    font-size: 17px;
    line-height: 36px;
}
.conversation.z-panel .z-caption-content i {
    width : 28px;
}
.conversation.z-panel .body > .text {
    padding-left: 0px;
    padding-bottom: 0px;
}
.conversation.z-panel .body > .text:after {
    display: none;
}
.conversation.z-panel .action {
    border-top: 1px solid #E5E5E5;
    background-color: #F5F5F5;
    padding: 10px 12px 12px 12px;
}
.conversation.z-panel .action .z-textbox {
    height: 34px;
    font-family: 'Open Sans';
    ${ t:borderRadius('0') };
color: #858585;
border: 1px solid #D5D5D5;
padding: 5px 4px;
line-height: 1.2;
font-size: 12px;
-webkit-box-shadow: none;
box-shadow: none;
-webkit-transition-duration: 0.1s;
transition-duration: 0.1s;
}
.conversation.z-panel .action .z-textbox:hover {
    border-color: #B5B5B5;
}
.conversation.z-panel .action .z-textbox:focus {
    -webkit-box-shadow: none;
    box-shadow: none;
    color: #696969;
    border-color: #F59942;
    outline: none;
}
.conversation.z-panel .action .z-button {
    height: 34px;
}
.tooltip {
    opacity: 1 !important;
    filter: alpha(opacity=100) !important;
}
.tooltip-success.tooltip > .tooltip-content > .tooltip-inner {
    background-color: #629b58;
    color: #FFF;
    text-shadow: 1px 1px 0 rgba(60, 100, 20, 0.3);
    ${ t:borderRadius('0') };
}
.tooltip-success.tooltip.right .tooltip-arrow {
    border-right-color: #629b58;
}
.tooltip-success.tooltip.left .tooltip-arrow {
    border-left-color: #629b58;
}
.tooltip-error.tooltip > .tooltip-content > .tooltip-inner {
    background-color: #c94d32;
    color: #FFF;
    text-shadow: 1px 1px 0 rgba(100, 60, 20, 0.3);
    ${ t:borderRadius('0') };
}
.tooltip-error.tooltip.right .tooltip-arrow {
    border-right-color: #c94d32;
}
.tooltip-error.tooltip.left .tooltip-arrow {
    border-left-color: #c94d32;
}
.tooltip-warning.tooltip > .tooltip-content > .tooltip-inner {
    background-color: #ed9421;
    color: #FFF;
    text-shadow: 1px 1px 0 rgba(100, 90, 10, 0.3);
    ${ t:borderRadius('0') };
}
.tooltip-warning.tooltip.right .tooltip-arrow {
    border-right-color: #ed9421;
}
.tooltip-warning.tooltip.left .tooltip-arrow {
    border-left-color: #ed9421;
}
.cal.tooltip {
    margin-left: -22px;
}
.cal.tooltip > .tooltip-content > .tooltip-inner {
    font-weight: bold;
}

.z-grid,
.z-listbox {
    border: 0;
}
.z-grid .z-row > .z-row-inner,
.z-grid .z-row > .z-cell {
    background-image: none;
}
.z-grid .z-row:hover > .z-row-inner,
.z-grid .z-row:hover > .z-cell {
    background-image: none;
}
.z-grid .z-column-content {
    padding: 8px;
    line-height: 1.5;
}
.z-grid .z-row.z-grid-odd > .z-row-inner,
.z-grid .z-row.z-grid-odd > .z-row-inner:hover {
    background-color: transparent;
}
.z-grid .z-grid-header,
.z-grid .z-column {
    background: none;
    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
}
.z-grid .z-column-content {
    font-family: 'Open Sans';
    font-size: 13px;
    color: #707070;
}
.z-grid .z-column-content [class*="z-icon-"] {
    margin-right: 2px;
}

.z-grid-header-border {
    border-bottom: 0px solid #CFCFCF !important;
    
}
.z-caption-content, .z-caption .z-label {
    display: inline-block;
    padding: 0px;
    line-height: 24px;
    float: none !important;
    margin-left:15px;
    font-weight:bold;

}

 .sub-modu a span {
    margin-left: 15px !important;
}



.comments.z-grid .user > .z-image {
    border-color: #5293C4;
}
.comments.z-grid .z-row:hover .tools {
    display: inline-block;
}
.comments.z-grid .z-row-content {
    padding: 0;
    min-height: 66px;
    position: relative;
}


.dialog.z-grid {
    border-spacing: 12px;
}
.dialog.z-grid:before {
    display: block;
    width: 3px;
    height: 100%;
    position: absolute;
    content: "";
    left: 30px;
    max-width: 3px;
    background-color: #E1E6ED;
    border: 1px solid #D7DBDD;
    border-width: 0 1px;
}
.dialog.z-grid .z-grid-body > table {
    border-collapse: separate;
    border-spacing: 3px 12px;
}
.dialog.z-grid .z-row-inner {
    vertical-align: top;
    overflow: visible;
}
.dialog.z-grid .z-row-content {
    padding: 0;
    overflow: visible;
}
.dialog.z-grid .z-grid-body {
    padding: 9px 9px 0 9px;
}
.dialog.z-grid .z-row {
    min-height: 66px;
    position: relative;
}
.dialog.z-grid .z-row:hover .tools {
    display: inline-block;
}
.dialog.z-grid .body {
    position: relative;
    width: auto;
    padding: 5px 8px 8px;
    border: 1px solid #DDE4ED;
    border-left-width: 2px;
    margin-right: 1px;
}

.dialog.z-grid .body > .name {
    display: block;
    color:#1f1968 !important;
}

.dialog.z-grid .body > .name:hover {
    
}

.dialog.z-grid .body > .name:visited {
    color:#1f1988 !important;
}

.dialog.z-grid .body .tools {
    bottom: 4px;
    ${ t:borderRadius('36px') };
margin: 1px 0px;
}
.dialog.z-grid .body:before {
    content: "";
    display: block;
    width: 8px;
    height: 8px;
    position: absolute;
    left: -7px;
    top: 11px;
    border: 2px solid #DDE4ED;
    border-width: 2px 0 0 2px;
    background-color: #FFF;
    -webkit-box-sizing: content-box;
    -moz-box-sizing: content-box;
    box-sizing: content-box;
    -webkit-transform: rotate(-45deg);
    -ms-transform: rotate(-45deg);
    transform: rotate(-45deg);
}

.user {
    display: inline-block;
    width: 42px;
}
.user > .z-image {
    ${ t:borderRadius('100%') };
border: 2px solid #C9D6E5;
max-width: 40px;
}

.time {
    display: block;
    font-size: 11px;
    font-weight: bold;
    position: absolute;
    right: 9px;
    top: 0;
    cursor: default;
}
.time [class*="z-icon-"] {
    font-size: 14px;
    color: #666666;
    margin-right: 4px;
}
.blue {
    color: #478FCA!important;
}
.green {
    color: #69AA46 !important;
}
.grey {
    color: #777 !important;
}
.purple {
    color: #A069C3 !important;
}
.red {
    color: #DD5A43 !important;
}
.orange {
    color: #FF892A !important;
}
.lighter {
    font-weight: lighter;
}

.body > .tools {
    right: 9px;
    bottom: 10px;
}

.comments.z-grid .z-row:hover .tools {
    display: inline-block;
}.tools {
    position: absolute;
    right: 5px;
    display: none;
    bottom: 4px;
}
.tools .btn {
    ${ t:borderRadius('36px') };
margin: 1px 0;
}
.action a {
    margin: 0 3px;
    display: inline-block;
    opacity: 0.85;
    filter: alpha(opacity=85);
    -webkit-transition: all 0.1s;
    transition: all 0.1s;
}
.action a:hover {
    text-decoration: none;
    opacity: 1;
    filter: alpha(opacity=100);
}



.body {
    position: relative;
    width: auto;
    margin-right: 12px;
}

.body > .text {
    display: block;
    position: relative;
    margin-top: 2px;
    padding-bottom: 19px;
    padding-left: 7px;
    font-size: 13px;
}
.body > .text > [class*="z-icon-quote-"]:first-child {
    color: #000000;
    margin-right: 4px;
}
.body > .text:after {
    display: block;
    content: '';
    height: 1px;
    font-size: 0px;
    overflow: hidden;
    position: absolute;
    left: 16px;
    right: -12px;
    margin-top: 9px;
    border-top: 1px solid #e4ecf3;
}
.body > .tools {
    right: 9px;
    bottom: 10px;
}


.double16.z-separator-horizontal-bar {
    height: 16px;
}
.double16.z-separator-horizontal-bar:before {
    height: 3px;
    border-bottom: 1px dotted #E3E3E3;
    top: 6px;
}

.double17.z-separator-horizontal-bar {
    height: 4px;
}
.double17.z-separator-horizontal-bar:before {
    height: 2px;
    border-bottom: 1px dotted #E3E3E3;
    top: 2px;
}

.solid.z-separator-horizontal-bar:before {
    border-style: solid;
}


.link.z-a:hover {
    text-decoration: underline;
}

.label-death{
margin-right:5px ;
}
.label-info{
margin-right:5px ;
}
.label-warning{
margin-left:5px ;
}
.task.z-listbox {
    border-bottom: 1px solid #DDD;
}
.task.z-listbox .z-listitem:hover .z-listitem-checkable {
    border-color: #FF893C;
}
.task.z-listbox .z-listitem-checkbox {
    width: 18px;
    height: 18px;
}
.task.z-listbox .z-listitem {
    border: 1px solid #DDD;
    border-left-width: 3px;
}
.task.z-listbox .z-listitem-selected {
    color: #8090A0;
    background-color: #F4F9FC;
}
.task.z-listbox .z-listitem-selected > .z-listcell,
.task.z-listbox .z-listitem-selected:hover > .z-listcell {
    background-image: none;
}
.task.z-listbox .z-listitem-selected .z-listcell.text .z-listcell-content {
    text-decoration: line-through;
    color: #000000;
}
.task.z-listbox .z-listitem .z-listcell + .z-listcell {
    border-left: 0;
}
.task.z-listbox .z-listitem .z-listcell-content {
    padding: 2px;
    
}
.task.z-listbox .z-listitem-icon {
    width: 16px;
    height: 16px;
    line-height: 16px;
    text-align: center;
    padding: 0;
}
.task.z-listbox .z-listitem .z-listitem-icon.z-icon-check:before {
    content: "\f00c";
}
.task.z-listbox .z-listitem,
.task.z-listbox .z-listitem:hover {
    border-left-width: 3px;
}
.task.z-listbox .task-orange {
    border-left-color: #e8b110;
}
.task.z-listbox .task-red {
    border-left-color: #d53f40;
}
.task.z-listbox .task-green {
    border-left-color: #9abc32;
}
.task.z-listbox .task-blue {
    border-left-color: #4f99c6;
}
.task.z-listbox .task-pink {
    border-left-color: #cb6fd7;
}
.task.z-listbox .task-grey {
    border-left-color: #a0a0a0;
}
.task.z-listbox .task-default {
    border-left-color: #abbac3;
}
.task.z-listbox-odd.z-listitem{
    background-color:none !important;
}


