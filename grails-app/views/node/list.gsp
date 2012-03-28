
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-node" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'node.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="nodetype" title="${message(code: 'node.nodetype.name.label', default: 'Nodetype')}" />
					
						<th><g:message code="node.template.label" default="Template" /></th>
					
						<g:sortableColumn property="status" title="${message(code: 'node.status.label', default: 'Status')}" />
					
						<g:sortableColumn property="importance" title="${message(code: 'node.importance.label', default: 'Importance')}" />
					
						<g:sortableColumn property="tags" title="${message(code: 'node.tags.label', default: 'Tags')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nodeInstanceList}" status="i" var="nodeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nodeInstance.id}">${fieldValue(bean: nodeInstance, field: "name")}</g:link></td>
					
						<td><g:link controller="nodetype" action="show" id="${nodeInstance.nodetype.id}">${nodeInstance.nodetype.name}</g:link></td>
					
						<td>${fieldValue(bean: nodeInstance, field: "template")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "status")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "importance")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "tags")}</td>
					
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
