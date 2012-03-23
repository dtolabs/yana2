
<%@ page import="com.dtosolutions.NodeType" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-nodeType" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list nodeType">
			
				<g:if test="${nodeTypeInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="nodeType.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${nodeTypeInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="nodeType.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${nodeTypeInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="nodeType.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${nodeTypeInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeInstance?.nodes}">
				<li class="fieldcontain">
					<span id="nodes-label" class="property-label"><g:message code="nodeType.nodes.label" default="Nodes" /></span>
					
						<g:each in="${nodeTypeInstance.nodes}" var="n">
						<span class="property-value" aria-labelledby="nodes-label"><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeInstance?.templates}">
				<li class="fieldcontain">
					<span id="templates-label" class="property-label"><g:message code="nodeType.templates.label" default="Templates" /></span>
					
						<g:each in="${nodeTypeInstance.templates}" var="t">
						<span class="property-value" aria-labelledby="templates-label"><g:link controller="template" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${nodeTypeInstance?.id}" />
					<g:link class="edit" action="edit" id="${nodeTypeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
