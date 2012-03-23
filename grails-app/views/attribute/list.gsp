
<%@ page import="com.dtosolutions.Attribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attribute.label', default: 'Attribute')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-attribute" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-attribute" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'attribute.name.label', default: 'Name')}" />
					
						<th><g:message code="attribute.filter.label" default="Filter" /></th>
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'attribute.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="dateModified" title="${message(code: 'attribute.dateModified.label', default: 'Date Modified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${attributeInstanceList}" status="i" var="attributeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${attributeInstance.id}">${fieldValue(bean: attributeInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: attributeInstance, field: "filter")}</td>
					
						<td><g:formatDate date="${attributeInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${attributeInstance.dateModified}" /></td>
					
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
