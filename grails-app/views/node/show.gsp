
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

	<div id="show-template" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
		</g:if>
		<table class="scaffold" border="0">
			<tr class="results">
				<td style="font-weight:bold;"><g:message code="node.name.label" default="Name" />: </td>
				<td><g:fieldValue bean="${nodeInstance}" field="name"/></td>
			</tr>

			<g:if test="${nodeInstance?.description}">
			<tr>
					<td style="font-weight:bold;"><g:message code="node.description.label" default="Description" />: </td>
					<td><g:fieldValue bean="${nodeInstance}" field="description"/></td>
			</tr>
			</g:if>

			<tr>
				<td style="font-weight:bold;"><g:message code="node.template.label" default="Template" />: </td>
				<td><g:link controller="template" action="show" id="${nodeInstance?.template?.id}">${nodeInstance?.template?.encodeAsHTML()}</g:link></td>
			</tr>
			<tr>
				<td style="font-weight:bold;"><g:message code="node.status.label" default="Status" />: </td>
				<td><g:fieldValue bean="${nodeInstance}" field="status"/></td>
			</tr>
			<tr>
				<td style="font-weight:bold;"><g:message code="node.importance.label" default="Importance" />: </td>
				<td><g:fieldValue bean="${nodeInstance}" field="importance"/></td>
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
			
			<g:if test="${nodeInstance?.parent}">
			<tr>
				<td style="font-weight:bold;"><g:message code="node.parent.label" default="Parent" />: </td>
				<td><g:link controller="node" action="show" id="${nodeInstance?.parent?.id}">${nodeInstance?.parent?.encodeAsHTML()}</g:link></td>
			</tr>
			</g:if>

			<tr>
				<td style="font-weight:bold;"><g:message code="node.dateCreated.label" default="Date Created" />: </td>
				<td><g:formatDate date="${nodeInstance?.dateCreated}" /></td>
			</tr>
		
			<tr>
				<td style="font-weight:bold;"><g:message code="node.dateModified.label" default="Date Modified" />: </td>
				<td><g:formatDate date="${nodeInstance?.dateModified}" /></td>
			</tr>


			<g:if test="${nodeInstance?.nodes}">
			<tr>
				<td colspan=2>
					<ul>
					<g:each in="${nodeInstance.nodes}" var="n">
					<li><span class="property-value" aria-labelledby="nodes-label"><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></span></li>
					</g:each>
					</ul>
				</td>
			</tr>
			</g:if>
			
			<g:if test="${nodeInstance?.templateValues}">
				<g:each in="${nodeInstance.templateValues}" var="t">
					<tr>
						<td style="font-weight:bold;">${com.dtosolutions.Attribute.get(t?.templateattribute?.attribute?.id)}: </td>
						<td>${t?.value?.encodeAsHTML()}</td>
					</tr>
				</g:each>
			</g:if>
			
			<tr>
				<td colspan=2>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${nodeInstance?.id}" />
					<g:link class="edit" action="edit" id="${nodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
				</td>
			</tr>
		</table>
	</div>



	</body>
</html>
