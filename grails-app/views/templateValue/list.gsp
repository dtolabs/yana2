
<%@ page import="com.dtosolutions.TemplateValue" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'templateValue.label', default: 'TemplateValue')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-templateValue" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-templateValue" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="templateValue.node.label" default="Node" /></th>
					
						<th><g:message code="templateValue.templateattribute.label" default="Templateattribute" /></th>
					
						<g:sortableColumn property="value" title="${message(code: 'templateValue.value.label', default: 'Value')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'templateValue.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="dateModified" title="${message(code: 'templateValue.dateModified.label', default: 'Date Modified')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${templateValueInstanceList}" status="i" var="templateValueInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${templateValueInstance.id}">${fieldValue(bean: templateValueInstance, field: "node")}</g:link></td>
					
						<td>${fieldValue(bean: templateValueInstance, field: "templateattribute")}</td>
					
						<td>${fieldValue(bean: templateValueInstance, field: "value")}</td>
					
						<td><g:formatDate date="${templateValueInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${templateValueInstance.dateModified}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${templateValueInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
