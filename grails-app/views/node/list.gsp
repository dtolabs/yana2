
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-node" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-node" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'node.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="description" title="${message(code: 'node.description.label', default: 'Description')}" />
					
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
					
						<td>${fieldValue(bean: nodeInstance, field: "description")}</td>
					
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
