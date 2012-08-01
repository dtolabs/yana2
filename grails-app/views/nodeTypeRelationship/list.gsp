
<%@ page import="com.dtolabs.NodeTypeRelationship" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-nodeTypeRelationship" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="roleName" title="${message(code: 'nodeTypeRelationship.name.label', default: 'Role Name')}" />
															
						<th><g:message code="nodeTypeRelationship.parent.label" default="Parent" /></th>
					
						<th><g:message code="nodeTypeRelationship.child.label" default="Child" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nodeTypeRelationshipInstanceList}" status="i" var="nodeTypeRelationshipInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nodeTypeRelationshipInstance.id}">${fieldValue(bean: nodeTypeRelationshipInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: nodeTypeRelationshipInstance, field: "parent")}</td>
					
						<td>${fieldValue(bean: nodeTypeRelationshipInstance, field: "child")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nodeTypeRelationshipInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
