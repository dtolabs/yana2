
<%@ page import="com.dtosolutions.TemplateAttribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'templateAttribute.label', default: 'TemplateAttribute')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-templateAttribute" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list templateAttribute">
			
				<g:if test="${templateAttributeInstance?.attribute}">
				<li class="fieldcontain">
					<span id="attribute-label" class="property-label"><g:message code="templateAttribute.attribute.label" default="Attribute" /></span>
					
						<span class="property-value" aria-labelledby="attribute-label"><g:link controller="attribute" action="show" id="${templateAttributeInstance?.attribute?.id}">${templateAttributeInstance?.attribute?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateAttributeInstance?.template}">
				<li class="fieldcontain">
					<span id="template-label" class="property-label"><g:message code="templateAttribute.template.label" default="Template" /></span>
					
						<span class="property-value" aria-labelledby="template-label"><g:link controller="nodeType" action="show" id="${templateAttributeInstance?.template?.id}">${templateAttributeInstance?.template?.name?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateAttributeInstance?.required}">
				<li class="fieldcontain">
					<span id="required-label" class="property-label"><g:message code="templateAttribute.required.label" default="Required" /></span>
					
						<span class="property-value" aria-labelledby="required-label"><g:formatBoolean boolean="${templateAttributeInstance?.required}" /></span>
					
				</li>
				</g:if>
			
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${templateAttributeInstance?.id}" />
					<g:link class="edit" action="edit" id="${templateAttributeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
