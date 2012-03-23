
<%@ page import="com.dtosolutions.TemplateAttribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'templateAttribute.label', default: 'TemplateAttribute')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-templateAttribute" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="templateAttribute.attribute.label" default="Attribute" /></th>
					
						<th><g:message code="templateAttribute.template.label" default="Template" /></th>
					
						<g:sortableColumn property="required" title="${message(code: 'templateAttribute.required.label', default: 'Required')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${templateAttributeInstanceList}" status="i" var="templateAttributeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${templateAttributeInstance.id}">${fieldValue(bean: templateAttributeInstance, field: "attribute")}</g:link></td>
					
						<td>${fieldValue(bean: templateAttributeInstance, field: "template")}</td>
					
						<td><g:formatBoolean boolean="${templateAttributeInstance.required}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${templateAttributeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
