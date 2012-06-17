www<%@ page import="com.dtolabs.Attribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attribute.label', default: 'Attribute')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-attribute" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'attribute.name.label', default: 'Name')}" />
					
						<th><g:message code="attribute.filter.label" default="Filter" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${attributeInstanceList}" status="i" var="attributeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${attributeInstance.id}">${fieldValue(bean: attributeInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: attributeInstance, field: "filter")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${attributeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
