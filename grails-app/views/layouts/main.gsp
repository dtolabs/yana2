<!DOCTYPE html>
<html>
  <head>
    <title>YANA <g:layoutTitle default="Welcome" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'yana.css')}" />
    <g:render template="/common/css"/>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}?v=2" />
    <g:javascript library="jquery" plugin="jquery"/>
   	<r:require module="jquery-ui"/>

    <r:layoutResources/>
	<g:javascript src="jquery.json-2.3.js"/>
    <g:layoutHead />
	

	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'superfish.css')}" />
	<script type="text/javascript" src="${resource(dir:'js',file:'hoverIntent.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js',file:'superfish.js')}"></script>
	
	<script type="text/javascript">
	// initialise plugins
	jQuery(function(){
		jQuery('ul.sf-menu').superfish();
	});
	</script>
		
		
	<script type="text/javascript">
	$(document).ready(function() {
		$('ul.sf-menu').superfish({
			delay:       250,                            // one second delay on mouseout
			animation:   {opacity:'show',height:'show'},  // fade-in and slide-down animation
			speed:       'fast',                          // faster animation speed
			autoArrows:  false,                           // disable generation of arrow mark-up
			dropShadows: false                            // disable drop shadows
		});
	});
	
	</script>

  </head>
  <body>


<div id="fullheightcontainer">
  <div id="wrapper">
    <div id="outer">
      <div id="float-wrap">
        <div id="center">
          <div id="clearheadercenter"></div>
          <div id="container-center">
	    	<div id="header"><g:render template="/common/header" /></div>
		    <div id="body">
	
				<table border=0 width='100%' height='100%' cellspacing=0 cellpadding=20 valign=top align=middle>
					<tr><td width=100% height=100% valign=top><g:layoutBody /></td></tr>
				</table>
	
			</div>
			<div id="footer"><g:render template="/common/footer" /></div>
          </div>
          <div id="clearfootercenter"></div>
        </div>
        <div id="left">
          <div id="clearheaderleft"></div>
          <div id="container-left">&nbsp;</div>
          <div id="clearfooterleft"></div>
        </div>
      </div>
      <div id="right">
        <div id="clearheaderright"></div>
        <div id="container-right">&nbsp;</div>
        <div id="clearfooterright"></div>
      </div>
      <div class="clear">&nbsp;</div>
    </div>
    <div id="gfx_bg_middle">&nbsp;</div>
  </div>
  <div class="clear">&nbsp;</div>
</div>


   
  </body>
</html>