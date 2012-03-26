
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-node" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list node">
			
				<g:if test="${nodeInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="node.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${nodeInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="node.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${nodeInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.template}">
				<li class="fieldcontain">
					<span id="template-label" class="property-label"><g:message code="node.template.label" default="Template" /></span>
					
						<span class="property-value" aria-labelledby="template-label"><g:link controller="template" action="show" id="${nodeInstance?.template?.id}">${nodeInstance?.template?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="node.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${nodeInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.importance}">
				<li class="fieldcontain">
					<span id="importance-label" class="property-label"><g:message code="node.importance.label" default="Importance" /></span>
					
						<span class="property-value" aria-labelledby="importance-label"><g:fieldValue bean="${nodeInstance}" field="importance"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.tags}">
				<li class="fieldcontain">
					<span id="tags-label" class="property-label"><g:message code="node.tags.label" default="Tags" /></span>
					
						<span class="property-value" aria-labelledby="tags-label"><g:fieldValue bean="${nodeInstance}" field="tags"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.nodetype}">
				<li class="fieldcontain">
					<span id="nodetype-label" class="property-label"><g:message code="node.nodetype.label" default="Nodetype" /></span>
					
						<span class="property-value" aria-labelledby="nodetype-label"><g:link controller="nodeType" action="show" id="${nodeInstance?.nodetype?.id}">${nodeInstance?.nodetype?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.parent}">
				<li class="fieldcontain">
					<span id="parent-label" class="property-label"><g:message code="node.parent.label" default="Parent" /></span>
					
						<span class="property-value" aria-labelledby="parent-label"><g:link controller="node" action="show" id="${nodeInstance?.parent?.id}">${nodeInstance?.parent?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="node.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${nodeInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="node.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${nodeInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.nodes}">
				<li class="fieldcontain">
					<span id="nodes-label" class="property-label"><g:message code="node.nodes.label" default="Nodes" /></span>
					
						<g:each in="${nodeInstance.nodes}" var="n">
						<span class="property-value" aria-labelledby="nodes-label"><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${nodeInstance?.templateValues}">
				<li class="fieldcontain">
					<span id="templateValues-label" class="property-label"><g:message code="node.templateValues.label" default="Template Values" /></span>
					
						<g:each in="${nodeInstance.templateValues}" var="t">
						<span class="property-value" aria-labelledby="templateValues-label"><g:link controller="templateValue" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${nodeInstance?.id}" />
					<g:link class="edit" action="edit" id="${nodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
