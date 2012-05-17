
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
			
			<g:if test="${nodeInstanceList}">
			<table width=100% valign=top>
				<tbody>
				<g:each in="${nodeInstanceList}" status="i" var="nodeInstance">
					<tr>
						<td width=16 valign=top><img src="${resource(dir:path,file:nodeInstance.nodetype.image)}" alt="" style="vertical-align:middle;"/></td>
						<td style="padding-left:5px;" width=200 valign=top><g:link controller="node" action="show" id="${nodeInstance.id}" style="padding:0;">${fieldValue(bean: nodeInstance, field: "name")}</g:link> [<g:link controller="nodeType" action="show" id="${nodeInstance.nodetype.id}" style="padding:0;">${nodeInstance.nodetype.name}</g:link>]
						<td style="padding-left:5px;" valign=top width=325><g:if test="${nodeInstance.description?.size()>50}">${nodeInstance.description[0..50]}...</g:if><g:else>${nodeInstance.description}</g:else></td>					
						<td style="padding-left:5px;" valign=top>
						<g:if test="${nodeInstance.tags}">
						<g:each in="${nodeInstance.tags.split(',')}" status="b" var="tag">
							<g:link controller="search" action="index" params="[q:tag]" style="padding:0;">${tag}</g:link><g:if test="${b+1<nodeInstance.tags.split(',').size()}">,</g:if>
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
			</g:if>
			<g:else>
				<span style="padding:25px;"><h4>No Nodes loaded. Please use the <g:link controller="import" action="importxml" style="font: bold 13px verdana, arial, helvetica, sans-serif">import tool</g:link> to load your initial structure.</h4></span>
			</g:else>
		</div>
	</body>
</html>
