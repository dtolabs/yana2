
<%@ page import="com.dtosolutions.Filter" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'filter.label', default: 'Filter')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="list-filter" class="list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="dataType" title="${message(code: 'filter.dataType.label', default: 'Data Type')}" />
					
						<g:sortableColumn property="regex" title="${message(code: 'filter.regex.label', default: 'Regex')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'filter.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="dateModified" title="${message(code: 'filter.dateModified.label', default: 'Date Modified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${filterInstanceList}" status="i" var="filterInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${filterInstance.id}">${fieldValue(bean: filterInstance, field: "dataType")}</g:link></td>
					
						<td>${fieldValue(bean: filterInstance, field: "regex")}</td>
					
						<td><g:formatDate date="${filterInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${filterInstance.dateModified}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${filterInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
