
<%@ page import="com.dtosolutions.NodeType" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-nodeType" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'nodeType.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="description" title="${message(code: 'nodeType.description.label', default: 'Description')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nodeTypeInstanceList}" status="i" var="nodeTypeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nodeTypeInstance.id}">${fieldValue(bean: nodeTypeInstance, field: "name")}</g:link></td>
						<g:if test="${nodeTypeInstance.description?.size()>50}">
						<td>${nodeTypeInstance.description[0..50]}...</td>
						</g:if>
						<g:else>
						<td>${nodeTypeInstance.description}</td>
						</g:else>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nodeTypeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
