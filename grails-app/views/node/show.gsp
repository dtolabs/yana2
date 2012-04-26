
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

	<div>
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
		</g:if>
		<table width="100%" border="0">
			<tbody>
			<tr class="results">
				<td style="font-weight:bold;" width="250"><g:message code="node.name.label" default="Name" />: </td>
				<td><g:fieldValue bean="${nodeInstance}" field="name"/></td>
			</tr>

			<g:if test="${nodeInstance?.description}">
			<tr>
					<td style="font-weight:bold;"><g:message code="node.description.label" default="Description" />: </td>
					<td><g:fieldValue bean="${nodeInstance}" field="description"/></td>
			</tr>
			</g:if>

			<tr>
				<td style="font-weight:bold;"><g:message code="node.status.label" default="Status" />: </td>
				<td><g:fieldValue bean="${nodeInstance}" field="status"/></td>
			</tr>
			
			<g:if test="${nodeInstance?.tags}">
			<tr>
					<td style="font-weight:bold;"><g:message code="node.tags.label" default="Tags" />: </td>
					<td><g:fieldValue bean="${nodeInstance}" field="tags"/></td>
			</tr>
			</g:if>
			
			<tr>
				<td style="font-weight:bold;"><g:message code="node.nodetype.label" default="Nodetype" />: </td>
				<td><g:link controller="nodeType" action="show" id="${nodeInstance?.nodetype?.id}">${nodeInstance?.nodetype?.encodeAsHTML()}</g:link></td>
			</tr>

			<tr>
				<td style="font-weight:bold;"><g:message code="node.dateCreated.label" default="Date Created" />: </td>
				<td><g:formatDate date="${nodeInstance?.dateCreated}" /></td>
			</tr>
		
			<tr>
				<td style="font-weight:bold;"><g:message code="node.dateModified.label" default="Date Modified" />: </td>
				<td><g:formatDate date="${nodeInstance?.dateModified}" /></td>
			</tr>
			
			<g:if test="${nodeInstance?.templateValues}">
				<g:each in="${nodeInstance.templateValues}" var="t">
				<g:set var="attribute" value="${com.dtosolutions.Attribute.get(t?.templateattribute?.attribute?.id)}" />
					<tr>
						<td><b>${attribute?.name}</b> [${attribute.filter.dataType}]: </td>
						<td>${t?.value?.encodeAsHTML()}</td>
					</tr>
				</g:each>
			</g:if>
			
			<tr>
				<td colspan=2><b>Node Parents</b><br>
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
				<td colspan=2><b>Node Children</b><br>
				<ul>
				<g:if test="${children}">
				<g:each in="${children}" status="i" var="child">
					<li class="fieldcontain"><span class="property-value" aria-labelledby="filter-label"><g:link controller="node" action="show" id="${child?.child?.id}">${child?.child?.name?.encodeAsHTML()} [${com.dtosolutions.NodeTypeRelationship.findByParentAndChild(child?.parent?.nodetype,child?.child?.nodetype).roleName}]</g:link></span></li>
				</g:each>
				</g:if>
				</ul>
				</td>
			</tr>
			
			<tr>
				<td colspan=2>
			<g:form>
				<fieldset class="form_footer">
					<g:hiddenField name="id" value="${nodeInstance?.id}" />
					<g:link class="fake_button" action="edit" id="${nodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
				</td>
			</tr>
			</tbody>
		</table>
	</div>



	</body>
</html>
