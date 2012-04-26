
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-node" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
				
						<g:sortableColumn property="name" title="${message(code: 'node.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="nodetype" title="${message(code: 'node.nodetype.name.label', default: 'Nodetype')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'node.status.label', default: 'Status')}" />
						
						<g:sortableColumn property="tags" title="${message(code: 'node.tags.label', default: 'Tags')}" />
						
						<g:sortableColumn property="description" title="${message(code: 'node.description.label', default: 'Description')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nodeInstanceList}" status="i" var="nodeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td><g:link controller="node" action="show" id="${nodeInstance.id}">${fieldValue(bean: nodeInstance, field: "name")}</g:link></td>
					
						<td><g:link controller="nodeType" action="show" id="${nodeInstance.nodetype.id}">${nodeInstance.nodetype.name}</g:link></td>
					
						<td>${fieldValue(bean: nodeInstance, field: "status")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "tags")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "description")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nodeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
