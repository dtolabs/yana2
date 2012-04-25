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
		<td width='*'>
			<center>
			<div>
			  <g:form url='[controller: "search", action: "index"]' id="searchableForm" name="searchableForm" method="get">
			      <g:textField name="q" value="${params.q}" size="50"/> <input type="submit" value="Search" />
			  </g:form>
			  <div style="clear: both; display: none;" class="hint">See <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Lucene query syntax</a> for advanced queries</div>
			</div>
			</center>
		</td>
	    <td width='280' >
	      <div style="position:relative;top:5px;float:right;padding:20px;">
	        <sec:ifLoggedIn>
				Welcome back, <sec:username/> | <a href="${createLink(controller:'logout',action:'index')}">Logout</a>
	        </sec:ifLoggedIn>
	        <sec:ifNotLoggedIn>
	          <a href="${createLink(controller:'login',action:'auth')}">Login</a>
	        </sec:ifNotLoggedIn>
	      </div>
	    </td>
	</tr>
	<tr>
		<td align=left width=255 height=24 style="background-color:#d6d6d6" valign=top><img src="<g:createLinkTo dir='images' file='yana_logo2.png'/>" width='46px' height='24px'/></td>
		<td height=24 bgcolor="#d6d6d6" colspan=2>

						<div id="smoothmenu" class="ddsmoothmenu">
						<ul>
						<li><g:link controller="node" action="list">Nodes</g:link>
						  <ul>
						  	<li><g:link controller="node" action="list">List</g:link></li>
							<li><g:link controller="node" action="create">Create</g:link></li>
							<li><g:link controller="childNode" action="list">Node Relationships</g:link>
								<ul>
									<li><g:link controller="childNode" action="create">Create Node Relationship</g:link></li>
								</ul>
							</li>
						  </ul>
						</li>
                      	<li><g:link controller="nodeType" action="list">Types</g:link>
						    <ul>
						    <li><g:link controller="nodeType" action="list">NodeTypes</g:link>
								<ul>
									<li><g:link controller="nodeType" action="create">Create NodeType</g:link></li>
								</ul>
							</li>
						    <li><g:link controller="attribute" action="list">Attributes</g:link>
								<ul>
									<li><g:link controller="attribute" action="create">Create Attribute</g:link></li>
								</ul>
							</li>
						    <li><g:link controller="filter" action="list">Filters</g:link>
								<ul>
									<li><g:link controller="filter" action="create">Create Filter</g:link></li>
								</ul>
							</li>
						    <li><g:link controller="nodeTypeRelationship" action="list">Nodetype Relationship</g:link>
								<ul>
									<li><g:link controller="nodeTypeRelationship" action="create">Create Nodetype Relationship</g:link></li>
								</ul>
							</li>
						    </ul>

						</li>
						<li><a href="${grailsApplication.config.grails.serverURL}/import">Admin</a>
						  <ul>
						  <li><g:link controller="import" action="importxml">Import Resources</g:link></li>
						  <li><g:link controller="user" action="search">Users</g:link>
								<ul>
									<li><g:link controller="user" action="create">Create User</g:link></li>
								</ul>
						  </li>
						  <li><g:link controller="role" action="search">Roles</g:link>
								<ul>
									<li><g:link controller="role" action="create">Create Role</g:link></li>
								</ul>
						  </li>
						  <li><g:link controller="webhook" action="list">Webhooks</g:link>
								<ul>
									<li><g:link controller="webhook" action="create">Create Webhook</g:link></li>
								</ul>
						  </li>
						  </ul>
						</li>
						</ul>
						<br style="clear: left" />
						</div>


		</td>
	</tr>
</table>
<div style="position:absolute;left:7px;top:85px;width:600px;text-align:left;"><dto:breadcrumbs>${it}</dto:breadcrumbs></div>
