<%@ page import="com.dtolabs.ChildNode" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'childNode.label', default: 'ChildNode')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-childNode" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="relationshipName" title="${message(code: 'childNode.relationshipName.label', default: 'Relationship Name')}" />
					
						<th><g:message code="childNode.parent.label" default="Parent" /></th>
					
						<th><g:message code="childNode.child.label" default="Child" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${childNodeInstanceList}" status="i" var="childNodeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${childNodeInstance.id}">${fieldValue(bean: childNodeInstance, field: "relationshipName")}</g:link></td>
					
						<td>${fieldValue(bean: childNodeInstance, field: "parent")}</td>
					
						<td>${fieldValue(bean: childNodeInstance, field: "child")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${childNodeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
