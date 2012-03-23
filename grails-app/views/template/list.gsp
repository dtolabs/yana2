
<%@ page import="com.dtosolutions.Template" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'template.label', default: 'Template')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-template" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="templateName" title="${message(code: 'template.templateName.label', default: 'Template Name')}" />
					
						<th><g:message code="template.nodetype.label" default="Nodetype" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${templateInstanceList}" status="i" var="templateInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${templateInstance.id}">${fieldValue(bean: templateInstance, field: "templateName")}</g:link></td>
					
						<td>${fieldValue(bean: templateInstance, field: "nodetype")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${templateInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
