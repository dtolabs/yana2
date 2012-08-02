
<%@ page import="com.dtolabs.Node" %>
<%@ page import="com.dtolabs.NodeTypeRelationship" %>

<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

	<div id="show-nodeType" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
		</g:if>
		

		<table width="100%" border="0" cellspacing=0 cellpadding=0 valign=top>
			<tr>
				<td valign=top>
					<div style="clear: left;">
					<table class="scaffold" width="450" border="0" cellspacing=5>
						<g:if test="${nodeInstance.nodetype.image}">
						<tr style="background-color:#021faf;">
							<td style="padding:10px;">
							<img src="${resource(dir:path,file:nodeInstance.nodetype.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" />
							<span class="image-title">${nodeInstance.name} [<g:link controller="nodeType" action="show" id="${nodeInstance.nodetype.id}" style="font: bold 17px verdana, arial, helvetica, sans-serif">${nodeInstance.nodetype.name}</g:link>]</span>
							<g:if test="${nodeInstance.nodetype.description}"><div class="image-description">${nodeInstance.nodetype.description}</div></g:if><br clear=left>

<div style="padding-top:5px;">
	<label for="id">
		<b>ID:</b>
	</label>
	<g:fieldValue bean="${nodeInstance}" field="id"/>
</div>

<div style="padding-top:5px;">
	<label for="tags">
		<b><g:message code="node.tags.label" default="Tags" />:</b>
	</label>
	<g:each in="${taglist}" status="i" var="t">
		<a href="/search/index?q=tags:${t}" style="padding:0;">${t}</a><g:if test="${i+1<taglist.size()}">,</g:if>
	</g:each>
</div>
							</td>
						<tr>
						</g:if>
						<tr>
							<td>
			
								<table width=100%>
									<g:if test="${nodeInstance?.nodeValues}">
		
										<g:each in="${com.dtolabs.NodeValue.findAllByNode(com.dtolabs.Node.get(nodeInstance?.id), [sort:'nodeattribute.attribute.name',order:'asc'])}" status="i" var="t">
										<g:set var="attribute" value="${com.dtolabs.Attribute.findAllById(t?.nodeattribute?.attribute?.id, [sort:'name',order:'asc'])}" />
												
										<tr>
											<td>${attribute.name[0]}</td>
											<td>${t.value.encodeAsHTML()}</td>
										</tr>
										</g:each>
									</g:if>
								</table>
			
							</td>
						</tr>
						<tr>
							<td>

								<table width=375>
									<thead>
										
										<tr>
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="node.dateCreated.label" default="Date Created" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${nodeInstance?.dateCreated}" /></td>
										</tr>
									
										<tr>
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="node.lastUpdated.label" default="Last Updated" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${nodeInstance?.lastUpdated}" /></td>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
					</table>
					</div>
				</td>
				<td valign=top width="275">
					<h3 style="padding:0;margin:0;">Relationships</h3>
					<table width="275" cellspacing=5 style="border: 1px solid #0431f7;">
						<tr>
							<td><h3 style="padding:0;margin:0;">Parents</h3>
								<ul style="list-style-type:none;padding:0;margin:0;float:left;">
								<g:if test="${parents}">
								<g:each in="${parents}" status="i" var="it">
									<li style="padding:0;margin:0;float:left;position:relative;left:-5px;">
									<img src="${resource(dir:smallpath,file:it.parent.nodetype.image)}" alt="" style="padding: 0px 10px 0px 7px;vertical-align:middle;" align="left" />
<g:link controller="node" action="show" id="${it?.parent?.id}">${it?.parent.encodeAsHTML()}</g:link></li>
								</g:each>
								</g:if>
								</ul>
							</td>
						</tr>
						<tr>
							<td><h3 style="padding:0;margin:0;">Children</h3>
								<ul style="list-style-type:none;padding:0;margin:0;float:left;">
								<g:if test="${children}">
								<g:each in="${children}" status="i" var="it">
									<li style="padding:0;margin:0;float:left;position:relative;left:-5px;"><span class="property-value" aria-labelledby="filter-label">
									<img src="${resource(dir:smallpath,file:it.child.nodetype.image)}" alt="" style="padding: 0px 10px 0px 7px;vertical-align:middle;" align="left" />
									<g:link controller="node" action="show" id="${it?.child?.id}">${it?.child.encodeAsHTML()}</g:link></li>
								</g:each>
								</g:if>
								</ul>
							</td>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<g:form action="delete">
						<fieldset class="form_footer">
							<g:hiddenField name="id" value="${nodeInstance?.id}" />
							<span class="fake_button" style="float:right;"><g:link action="clone" id="${nodeInstance?.id}"><g:message code="default.button.clone.label" default="Clone" /></g:link></span>
							<span class="fake_button"><g:link action="edit" id="${nodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>
							<g:submitButton name="delete" class="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>
