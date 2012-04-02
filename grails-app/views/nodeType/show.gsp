
<%@ page import="com.dtosolutions.NodeType" %>
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
			
		<table class="scaffold" border="0">
			<tr class="results">
				<td style="font-weight:bold;"><g:message code="nodeType.name.label" default="Name" />: </td>
				<td><g:fieldValue bean="${nodeTypeInstance}" field="name"/></td>
			</tr>

			<tr>
				<td style="font-weight:bold;"><g:message code="nodeType.dateCreated.label" default="Date Created" />: </td>
				<td><g:formatDate date="${nodeTypeInstance?.dateCreated}" /></td>
			</tr>
		
			<tr>
				<td style="font-weight:bold;"><g:message code="nodeType.dateModified.label" default="Date Modified" />: </td>
				<td><g:formatDate date="${nodeTypeInstance?.dateModified}" /></td>
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
			

			<tr>
				<td colspan=2>
					<g:form>
						<fieldset class="buttons">
							<g:hiddenField name="id" value="${nodeTypeInstance?.id}" />
							<g:link class="edit" action="edit" id="${nodeTypeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
							<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>
