
<%@ page import="com.dtolabs.NodeAttribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeAttribute.label', default: 'NodeAttribute')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-nodeAttribute" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list nodeAttribute">
			
				<g:if test="${nodeAttributeInstance?.attribute}">
				<li class="fieldcontain">
					<span id="attribute-label" class="property-label"><g:message code="nodeAttribute.attribute.label" default="Attribute" /></span>
						<span class="property-value" aria-labelledby="attribute-label"><g:link controller="attribute" action="show" id="${nodeAttributeInstance?.attribute?.id}">${nodeAttributeInstance?.attribute?.encodeAsHTML()}</g:link></span>
				</li>
				</g:if>
			
				<g:if test="${nodeAttributeInstance?.template}">
				<li class="fieldcontain">
					<span id="template-label" class="property-label"><g:message code="nodeAttribute.template.label" default="Template" /></span>
					
						<span class="property-value" aria-labelledby="template-label"><g:link controller="nodeType" action="show" id="${nodeAttributeInstance?.template?.id}">${nodeAttributeInstance?.template?.name?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeAttributeInstance?.required}">
				<li class="fieldcontain">
					<span id="required-label" class="property-label"><g:message code="nodeAttribute.required.label" default="Required" /></span>
					
						<span class="property-value" aria-labelledby="required-label"><g:formatBoolean boolean="${nodeAttributeInstance?.required}" /></span>
					
				</li>
				</g:if>
			
			
			</ol>
			<g:form>
				<fieldset class="form_footer">
					<g:hiddenField name="id" value="${nodeAttributeInstance?.id}" />
					<span class="fake_button"><g:link action="edit" id="${nodeAttributeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
