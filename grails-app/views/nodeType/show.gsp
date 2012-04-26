
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
			
		<img src="${resource(dir:path,file:nodeTypeInstance.image)}" alt="" />

		<table class="scaffold" border="0">
			<tr class="results">
				<td style="font-weight:bold;"><g:message code="nodeType.name.label" default="Name" />: </td>
				<td><g:fieldValue bean="${nodeTypeInstance}" field="name"/></td>
			</tr>
			
			<g:if test="${nodeTypeInstance?.attributes}">
					<g:each in="${nodeTypeInstance.attributes}" var="a">
					<g:set var="attributeInstance" value="${com.dtosolutions.Attribute.get(a.attribute.id)}"/>
					<tr>
						<td><b>${attributeInstance.name}</b> [${attributeInstance.filter.dataType}]: </td>
						<td>${a.required.encodeAsHTML()}</td>
					</tr>
					</g:each>
			</g:if>
			
			<g:if test="${nodeTypeInstance?.description}">
			<tr>
					<td style="font-weight:bold;"><g:message code="nodeType.description.label" default="Description" />: </td>
					<td><g:fieldValue bean="${nodeTypeInstance}" field="description"/></td>
			</tr>
			</g:if>
			
			<tr>
				<td style="font-weight:bold;"><g:message code="nodeType.dateCreated.label" default="Date Created" />: </td>
				<td><g:formatDate date="${nodeTypeInstance?.dateCreated}" /></td>
			</tr>
		
			<tr>
				<td style="font-weight:bold;"><g:message code="nodeType.dateModified.label" default="Date Modified" />: </td>
				<td><g:formatDate date="${nodeTypeInstance?.dateModified}" /></td>
			</tr>

			<tr>
				<td colspan=2>
					<g:form>
						<fieldset class="form_footer">
							<g:hiddenField name="id" value="${nodeTypeInstance?.id}" />
							<g:link class="fake_button" action="edit" id="${nodeTypeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
							<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>
