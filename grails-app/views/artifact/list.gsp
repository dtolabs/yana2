
<%@ page import="com.dtosolutions.Artifact" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'artifact.label', default: 'Artifact')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-artifact" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="list-artifact" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'artifact.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="description" title="${message(code: 'artifact.description.label', default: 'Description')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'artifact.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="dateModified" title="${message(code: 'artifact.dateModified.label', default: 'Date Modified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${artifactInstanceList}" status="i" var="artifactInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${artifactInstance.id}">${fieldValue(bean: artifactInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: artifactInstance, field: "description")}</td>
					
						<td><g:formatDate date="${artifactInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${artifactInstance.dateModified}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${artifactInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
