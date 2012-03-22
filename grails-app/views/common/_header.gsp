<style type="text/css">
	.topnav {
	    background:#615e54;
	    //margin:5px;
	    padding:5px 0px 5px 0px;
	    width:100%;
	    font: bold 13px helvetica, verdana, arial, sans-serif;
	}
</style>


<table border=0 cellspacing=0 cellpadding=0 width='100%' valign=top>
	<tr>
		<td align=left width=255 height=52 valign=bottom><img src="<g:createLinkTo dir='images' file='yana_logo1.png'/>" width='255px' height='52px' style="vertical-align:bottom;"/></td>
		<td width='*'>&nbsp;</td>
	</tr>
	<tr>
		<td align=left width=255 height=24 style="background-color:#d6d6d6" valign=top><img src="<g:createLinkTo dir='images' file='yana_logo2.png'/>" width='46px' height='24px'/></td>
		<td height=24 bgcolor="#d6d6d6">

			
					<sec:ifNotLoggedIn>
					          <li><g:link controller="login" action="auth">Login</g:link></li>
					</sec:ifNotLoggedIn>
			
					<sec:ifLoggedIn>

						<div id="smoothmenu" class="ddsmoothmenu">
						<ul>
						<sec:ifAnyGranted roles="ROLE_ROOT,ROLE_USER">
						<li><a href="${grailsApplication.config.grails.serverURL}/">Configuration</a>
						  <ul>
	                    	<li><g:link controller="solution" action="list">Solutions</g:link></li>
	                    	<li><g:link controller="location" action="list">Locations</g:link></li>
	                      	<li><g:link controller="node" action="list">Nodes</g:link></li>
	                      	<li><g:link controller="nodetype" action="list">Nodetypes</g:link></li>
	                      	<li><g:link controller="artifact" action="list">Artifacts</g:link></li>
	                      	<li><g:link controller="instances" action="list">Instances</g:link></li>
	                      	<li><g:link controller="template" action="list">Templates</g:link></li>
						  </ul>
						</li>
						</sec:ifAnyGranted>
						<sec:ifAnyGranted roles="ROLE_ROOT,ROLE_USER">
						<li><a href="${grailsApplication.config.grails.serverURL}/account">Account</a></li>
						</sec:ifAnyGranted>
						<sec:ifAnyGranted roles="ROLE_ROOT">
						<li><a href="${grailsApplication.config.grails.serverURL}/admin">Admin</a>
						  <ul>
						  <li><g:link controller="admin" action="importxml">Import Resources</g:link></li>
						  </ul>
						</li>
						</sec:ifAnyGranted>
						</ul>
						<br style="clear: left" />
						</div>
			          
			          </sec:ifLoggedIn>

		</td>
	</tr>
</table>
<div style="position:absolute;left:7px;top:125px;width:600px;text-align:left;"><gzo:breadcrumbs>${it}</gzo:breadcrumbs></div>
