<!DOCTYPE html>
<html>
  <head>
    <title>YANA <g:layoutTitle default="Welcome" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'yana.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <g:javascript library="jquery" plugin="jquery"/>
   	<r:require module="jquery-ui"/>

    <r:layoutResources/>
	<g:javascript src="jquery.json-2.3.js"/>
    <g:layoutHead />

  </head>
  <body>
   <div>
     <div id="hd">
       <a href="<g:createLinkTo dir="/"/>">
       <img id="logo"
	    src="${createLinkTo(dir: 'images', file: 'headerlogo.png')}"
	    alt="YANA logo"/>
     </a>
     </div>
     <div id="bd"><!--start body-->
     <g:layoutBody />
     </div><!--end body-->
     <div id="ft">
       <div id="footerText">
	 YANA - Yet another node authority!

	 &copy; Copyright 2010 <a href="https://github.com/dtolabs">DTO Labs</a>.
	 All rights reserved.
       </div>
     </div>
   </div>
  </body>
</html>