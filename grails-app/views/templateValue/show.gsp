
<%@ page import="com.dtosolutions.TemplateValue" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'templateValue.label', default: 'TemplateValue')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-templateValue" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-templateValue" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list templateValue">
			
				<g:if test="${templateValueInstance?.node}">
				<li class="fieldcontain">
					<span id="node-label" class="property-label"><g:message code="templateValue.node.label" default="Node" /></span>
					
						<span class="property-value" aria-labelledby="node-label"><g:link controller="node" action="show" id="${templateValueInstance?.node?.id}">${templateValueInstance?.node?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateValueInstance?.templateattribute}">
				<li class="fieldcontain">
					<span id="templateattribute-label" class="property-label"><g:message code="templateValue.templateattribute.label" default="Templateattribute" /></span>
					
						<span class="property-value" aria-labelledby="templateattribute-label"><g:link controller="templateAttribute" action="show" id="${templateValueInstance?.templateattribute?.id}">${templateValueInstance?.templateattribute?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateValueInstance?.value}">
				<li class="fieldcontain">
					<span id="value-label" class="property-label"><g:message code="templateValue.value.label" default="Value" /></span>
					
						<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${templateValueInstance}" field="value"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateValueInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="templateValue.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${templateValueInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${templateValueInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="templateValue.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${templateValueInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${templateValueInstance?.id}" />
					<g:link class="edit" action="edit" id="${templateValueInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
