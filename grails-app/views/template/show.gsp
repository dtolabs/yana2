
<%@ page import="com.dtosolutions.Template" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'template.label', default: 'Template')}" />
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
				<td style="font-weight:bold;"><g:message code="template.templateName.label" default="Template Name" />: </td>
				<td><g:fieldValue bean="${templateInstance}" field="templateName"/></td>
			</tr>

			<g:if test="${templateInstance?.attributes}">
					<g:each in="${templateInstance.attributes}" var="a">
					<tr>
						<td style="font-weight:bold;">${com.dtosolutions.Attribute.get(a.attribute.id)}: </td>
						<td>${a.required.encodeAsHTML()}</td>
					</tr>
					</g:each>
			</g:if>

			<tr>
				<td style="font-weight:bold;"><g:message code="template.nodetype.label" default="Nodetype" />: </td>
				<td><g:link controller="nodeType" action="show" id="${templateInstance?.nodetype?.id}">${templateInstance?.nodetype?.encodeAsHTML()}</g:link></td>
			</tr>
			<tr>
				<td colspan=2>
					<g:form>
					<fieldset class="buttons">
						<g:hiddenField name="id" value="${templateInstance?.id}" />
						<g:link class="edit" action="edit" id="${templateInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
					</g:form>
				</td>
			</tr>
		</table>
	</div>
	</body>
</html>
