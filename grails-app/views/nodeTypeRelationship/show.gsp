
<%@ page import="com.dtosolutions.NodeTypeRelationship" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-nodeTypeRelationship" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list nodeTypeRelationship">
			
				<g:if test="${nodeTypeRelationshipInstance?.roleName}">
				<li class="fieldcontain">
					<span id="roleName-label" class="property-label"><g:message code="nodeTypeRelationship.roleName.label" default="Role Name" /></span>
					
						<span class="property-value" aria-labelledby="roleName-label"><g:fieldValue bean="${nodeTypeRelationshipInstance}" field="roleName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeRelationshipInstance?.parentCardinality}">
				<li class="fieldcontain">
					<span id="parentCardinality-label" class="property-label"><g:message code="nodeTypeRelationship.parentCardinality.label" default="Parent Cardinality" /></span>
					
						<span class="property-value" aria-labelledby="parentCardinality-label"><g:fieldValue bean="${nodeTypeRelationshipInstance}" field="parentCardinality"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeRelationshipInstance?.childCardinality}">
				<li class="fieldcontain">
					<span id="childCardinality-label" class="property-label"><g:message code="nodeTypeRelationship.childCardinality.label" default="Child Cardinality" /></span>
					
						<span class="property-value" aria-labelledby="childCardinality-label"><g:fieldValue bean="${nodeTypeRelationshipInstance}" field="childCardinality"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeRelationshipInstance?.parent}">
				<li class="fieldcontain">
					<span id="parent-label" class="property-label"><g:message code="nodeTypeRelationship.parent.label" default="Parent" /></span>
					
						<span class="property-value" aria-labelledby="parent-label"><g:link controller="nodeType" action="show" id="${nodeTypeRelationshipInstance?.parent?.id}">${nodeTypeRelationshipInstance?.parent?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeTypeRelationshipInstance?.child}">
				<li class="fieldcontain">
					<span id="child-label" class="property-label"><g:message code="nodeTypeRelationship.child.label" default="Child" /></span>
					
						<span class="property-value" aria-labelledby="child-label"><g:link controller="nodeType" action="show" id="${nodeTypeRelationshipInstance?.child?.id}">${nodeTypeRelationshipInstance?.child?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="form_footer">
					<g:hiddenField name="id" value="${nodeTypeRelationshipInstance?.id}" />
					<g:link class="fake_button" action="edit" id="${nodeTypeRelationshipInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
