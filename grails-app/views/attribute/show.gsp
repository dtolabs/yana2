
<%@ page import="com.dtosolutions.Attribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attribute.label', default: 'Attribute')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-attribute" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list attribute">
			
				<g:if test="${attributeInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="attribute.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${attributeInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${attributeInstance?.filter}">
				<li class="fieldcontain">
					<span id="filter-label" class="property-label"><g:message code="attribute.filter.label" default="Filter" /></span>
					
						<span class="property-value" aria-labelledby="filter-label"><g:link controller="filter" action="show" id="${attributeInstance?.filter?.id}">${attributeInstance?.filter?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${attributeInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="attribute.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${attributeInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${attributeInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="attribute.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${attributeInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="form_footer">
					<g:hiddenField name="id" value="${attributeInstance?.id}" />
					<g:link class="fake_button" action="edit" id="${attributeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
