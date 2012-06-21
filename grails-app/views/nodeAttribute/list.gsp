
<%@ page import="com.dtolabs.NodeAttribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeAttribute.label', default: 'NodeAttribute')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-nodeAttribute" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="nodeAttribute.attribute.label" default="Attribute" /></th>
					
						<th><g:message code="nodeAttribute.template.label" default="Template" /></th>
					
						<g:sortableColumn property="required" title="${message(code: 'nodeAttribute.required.label', default: 'Required')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nodeAttributeInstanceList}" status="i" var="nodeAttributeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nodeAttributeInstance.id}">${fieldValue(bean: nodeAttributeInstance, field: "attribute")}</g:link></td>
					
						<td>${fieldValue(bean: nodeAttributeInstance, field: "template")}</td>
					
						<td><g:formatBoolean boolean="${nodeAttributeInstance.required}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nodeAttributeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
