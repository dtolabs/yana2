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
    
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'ddsmoothmenu.css')}" />
	<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'ddsmoothmenu-v.css')}" />
	
	<script type="text/javascript" src="/js/ddsmoothmenu.js"></script>
	
	<script type="text/javascript">
	
	ddsmoothmenu.init({
		mainmenuid: "smoothmenu", //menu DIV id
		orientation: 'h', //Horizontal or vertical menu: Set to "h" or "v"
		classname: 'ddsmoothmenu', //class added to menu's outer DIV
		customtheme: ["#d6d6d6", "#999"],
		contentsource: "markup" //"markup" or ["container_id", "path_to_menu_file"]
	})
	
	</script>

  </head>
  <body>
  
	<div id="container">
	    <div id="header"><g:render template="/common/header" /></div>
	    <div id="body">

			<table border=0 width='100%' cellspacing=0 cellpadding=20 valign=top align=middle>
				<tr><td width=100% valign=top><g:layoutBody /></td></tr>
			</table>

		</div>
		<div id="footer"><g:render template="/common/footer" /></div>
	</div>
   
  </body>
</html>