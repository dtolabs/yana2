
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

	<div id="show-nodeType" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
		</g:if>
		

		<table width="100%" border="0" cellspacing=5 valign=top>
			<tr>
				<td valign=top>
					<div style="clear: left;">
					<table class="scaffold" width="450" border="0" cellspacing=5>
						<g:if test="${nodeInstance.nodetype.image}">
						<tr style="background-color:#021faf;">
							<td style="padding:10px;">
							<img src="${resource(dir:path,file:nodeInstance.nodetype.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" />
							<span class="image-title">${nodeInstance.nodetype.name}</span>
							<g:if test="${nodeInstance.nodetype.description}"><div class="image-description">${nodeInstance.nodetype.description}</div></g:if>
<div style="padding-top:5px;">
	<label for="status">
		<b><g:message code="node.status.label" default="Status" />:</b>
	</label>
	<g:fieldValue bean="${nodeInstance}" field="status"/>
</div>

<div style="padding-top:5px;">
	<label for="tags">
		<b><g:message code="node.tags.label" default="Tags" />:</b>
	</label>
	<g:each in="${taglist}" status="i" var="t">
		<g:link controller="search" action="index" params="[q:t]">${t}</g:link><g:if test="${i<taglist.size()}">,</g:if>
	</g:each>
</div>
							</td>
						<tr>
						</g:if>
						<tr>
							<td>
			
								<table width=100%>
									<g:if test="${nodeInstance?.templateValues}">
		
										<g:each in="${com.dtosolutions.TemplateValue.findAllByNode(com.dtosolutions.Node.get(nodeInstance?.id), [sort:'templateattribute.attribute.name',order:'asc'])}" status="i" var="t">
										<g:set var="attribute" value="${com.dtosolutions.Attribute.findAllById(t?.templateattribute?.attribute?.id, [sort:'name',order:'asc'])}" />
												
										<tr>
											<td width="33%">${attribute.name[0]}</td>
											<td width="33%">${attribute.filter.dataType[0]}</td>
											<td width=33%">${t.value.encodeAsHTML()}</td>
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
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="node.dateModified.label" default="Date Modified" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${nodeInstance?.dateModified}" /></td>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<g:form>
									<fieldset class="form_footer">
										<g:hiddenField name="id" value="${nodeInstance?.id}" />
										<span class="fake_button"><g:link action="edit" id="${nodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>
										<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
									</fieldset>
								</g:form>
							</td>
						</tr>
					</table>
					</div>
				</td>
				<td valign=top>
					<h3>Relationships</h3>
					<table width="225" cellspacing=5 style="border: 1px solid #0431f7;">
						<tr>
							<td><h3>Parents</h3>
								<ul>
								<g:if test="${parents}">
								<g:each in="${parents}" status="i" var="parent">
									<li class="fieldcontain"><span class="property-value" aria-labelledby="filter-label"><g:link controller="node" action="show" id="${parent?.parent?.id}">${parent?.parent?.name?.encodeAsHTML()} [${com.dtosolutions.NodeTypeRelationship.findByParentAndChild(parent?.parent?.nodetype,parent?.child?.nodetype).roleName}]</g:link></span></li>
								</g:each>
								</g:if>
								</ul>
							</td>
						</tr>
						<tr>
							<td><h3>Children</h3>
								<ul>
								<g:if test="${children}">
								<g:each in="${children}" status="i" var="child">
									<li class="fieldcontain"><span class="property-value" aria-labelledby="filter-label"><g:link controller="node" action="show" id="${child?.child?.id}">${child?.child?.name?.encodeAsHTML()} [${com.dtosolutions.NodeTypeRelationship.findByParentAndChild(child?.parent?.nodetype,child?.child?.nodetype).roleName}]</g:link></span></li>
								</g:each>
								</g:if>
								</ul>
							</td>
					</table>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>
