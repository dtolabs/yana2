
<%@ page import="com.dtosolutions.Template" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'template.label', default: 'Template')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-template" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-template" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list template">
			
				<g:if test="${templateInstance?.templateName}">
				<li class="fieldcontain">
					<span id="templateName-label" class="property-label"><g:message code="template.templateName.label" default="Template Name" /></span>
					
						<span class="property-value" aria-labelledby="templateName-label"><g:fieldValue bean="${templateInstance}" field="templateName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateInstance?.attributes}">
				<li class="fieldcontain">
					<span id="attributes-label" class="property-label"><g:message code="template.attributes.label" default="Attributes" /></span>
					
						<g:each in="${templateInstance.attributes}" var="a">
						<span class="property-value" aria-labelledby="attributes-label"><g:link controller="templateAttribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${templateInstance?.nodetype}">
				<li class="fieldcontain">
					<span id="nodetype-label" class="property-label"><g:message code="template.nodetype.label" default="Nodetype" /></span>
					
						<span class="property-value" aria-labelledby="nodetype-label"><g:link controller="nodeType" action="show" id="${templateInstance?.nodetype?.id}">${templateInstance?.nodetype?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${templateInstance?.id}" />
					<g:link class="edit" action="edit" id="${templateInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
