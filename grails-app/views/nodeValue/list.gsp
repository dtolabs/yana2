
<%@ page import="com.dtolabs.NodeValue" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeValue.label', default: 'NodeValue')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-nodeValue" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-nodeValue" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="nodeValue.node.label" default="Node" /></th>
					
						<th><g:message code="nodeValue.nodeattribute.label" default="Nodeattribute" /></th>
					
						<g:sortableColumn property="value" title="${message(code: 'nodeValue.value.label', default: 'Value')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'nodeValue.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="dateModified" title="${message(code: 'nodeValue.dateModified.label', default: 'Date Modified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${nodeValueInstanceList}" status="i" var="nodeValueInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${nodeValueInstance.id}">${fieldValue(bean: nodeValueInstance, field: "node")}</g:link></td>
					
						<td>${fieldValue(bean: nodeValueInstance, field: "nodeattribute")}</td>
					
						<td>${fieldValue(bean: nodeValueInstance, field: "value")}</td>
					
						<td><g:formatDate date="${nodeValueInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${nodeValueInstance.dateModified}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nodeValueInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
