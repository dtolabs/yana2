
<%@ page import="com.dtosolutions.NodeType" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="list-nodeType" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<g:if test="${nodeTypeInstanceList}">
			<table>
				<tbody>
				<g:each in="${nodeTypeInstanceList}" status="i" var="nodeTypeInstance">
					<tr>
						<td style="padding-left:5px;">
						<img src="${resource(dir:path,file:nodeTypeInstance.image)}" alt="" style="vertical-align:middle;"/></td>
						<td style="padding-left:5px;"><g:link action="show" id="${nodeTypeInstance.id}">${fieldValue(bean: nodeTypeInstance, field: "name")}</g:link></td>
						<g:if test="${nodeTypeInstance.description?.size()>50}">
						<td style="padding-left:5px;">${nodeTypeInstance.description[0..50]}...</td>
						</g:if>
						<g:else>
						<td style="padding-left:5px;">${nodeTypeInstance.description}</td>
						</g:else>
						<td style="padding-left:5px;">(<a href="/search/index?q=nodetype:${nodeTypeInstance.name}">${com.dtosolutions.Node.countByNodetype(nodeTypeInstance)}</a>)</td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${nodeTypeInstanceTotal}" />
			</div>
			</g:if>
			<g:else>
				<span style="padding:25px;"><h4>No Nodetypes loaded. Please use the <g:link controller="import" action="importxml" style="font: bold 13px verdana, arial, helvetica, sans-serif">import tool</g:link> to load your initial structure.</h4></span>
			</g:else>
		</div>
	</body>
</html>
