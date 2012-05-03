
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-node" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table width=100%>

				<tbody>
				<g:each in="${nodeInstanceList}" status="i" var="nodeInstance">
					<tr>
						<td width=16><img src="${resource(dir:path,file:nodeInstance.nodetype.image)}" alt="" style="vertical-align:middle;"/></td>
						<td style="padding-left:5px;" width=200><g:link controller="node" action="show" id="${nodeInstance.id}">${fieldValue(bean: nodeInstance, field: "name")}</g:link> [<g:link controller="nodeType" action="show" id="${nodeInstance.nodetype.id}">${nodeInstance.nodetype.name}</g:link>]
						<td style="padding-left:5px;"><g:if test="${nodeInstance.description?.size()>50}">${nodeInstance.description[0..50]}...</g:if><g:else>${nodeInstance.description}</g:else></td>					
						<td style="padding-left:5px;">${fieldValue(bean: nodeInstance, field: "status")}</td>
						<td style="padding-left:5px;">
						<g:if test="${nodeInstance.tags}">
						<g:each in="${nodeInstance.tags.split(',')}" status="b" var="tag">
							<g:link controller="search" action="index" params="[q:tag]">${tag}</g:link><g:if test="${b+1<nodeInstance.tags.split(',').size()}">,</g:if>
						</g:each>
						</g:if>
						</td>
					
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
