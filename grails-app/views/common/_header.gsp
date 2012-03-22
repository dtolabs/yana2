<style type="text/css">
	.topnav {
	    background:#615e54;
	    //margin:5px;
	    padding:5px 0px 5px 0px;
	    width:100%;
	    font: bold 13px helvetica, verdana, arial, sans-serif;
	}
</style>


<table border=0 cellspacing=0 cellpadding=0 width='100%'>
	<tr>
		<td align=left width=255 height=52><img src="<g:createLinkTo dir='images' file='yana_logo1.png'/>" width='255px' height='52px'/></td>
		<td width='*'>&nbsp;</td>
	</tr>
	<tr>
		<td align=left width=255 height=24 style="background-color:#d6d6d6;"><img src="<g:createLinkTo dir='images' file='yana_logo2.png'/>" width='46px' height='24px'/></td>
		<td width='*'>
			<div class="topnav">
				<div id="nav">
			        <ul style="position:relative;top:-10px;">
			
					<sec:ifNotLoggedIn>
					          <li><a href="${grailsApplication.config.grails.serverURL}/login">Login</a></li>
					</sec:ifNotLoggedIn>
			
					<sec:ifLoggedIn>
			          <sec:ifAnyGranted roles="ROLE_USER">
			            <li><a href="${grailsApplication.config.grails.serverURL}/">Configuration</a>
			              <div class="drop"><div class="drop-t"></div>
			                <div class="drop-c">
			                  <div class="container">
			                    <strong class="title"></strong>
			                    <ul>
			                    	<li><g:link controller="solution" action="list">Solutions</g:link></li>
			                    	<li><g:link controller="location" action="list">Locations</g:link></li>
			                      	<li><g:link controller="node" action="list">Nodes</g:link></li>
			                      	<li><g:link controller="nodetype" action="list">Nodetypes</g:link></li>
			                      	<li><g:link controller="artifact" action="list">Artifacts</g:link></li>
			                      	<li><g:link controller="instances" action="list">Instances</g:link></li>
			                      	<li><g:link controller="template" action="list">Templates</g:link></li>
			                    </ul>
			                  </div>
			                </div>
			                <div class="drop-b">&nbsp;</div>
			              </div>
			            </li>
			          </sec:ifAnyGranted>
			
					<sec:ifAnyGranted roles="ROLE_ROOT,ROLE_USER">
			          <li><a href="${grailsApplication.config.grails.serverURL}/account">Account</a></li>
			        </sec:ifAnyGranted>
			        
			          <sec:ifAnyGranted roles="ROLE_ROOT">
			            <li><a href="${grailsApplication.config.grails.serverURL}/admin">Admin</a>
			              <div class="drop"><div class="drop-t"></div>
			                <div class="drop-c">
			                  <div class="container">
			                    <strong class="title"></strong>
			                    <ul>
			                    	<li><g:link controller="admin" action="importxml">Import Resources</g:link></li>
			                    </ul>
			                  </div>
			                </div>
			                <div class="drop-b">&nbsp;</div>
			              </div>
			            </li>
			          </sec:ifAnyGranted>
			          </sec:ifLoggedIn>
			
			        </ul>
				</div>
			</div>
		</td>
	</tr>
</table>
<div style="position:absolute;left:7px;top:125px;width:600px;text-align:left;"><gzo:breadcrumbs>${it}</gzo:breadcrumbs></div>
