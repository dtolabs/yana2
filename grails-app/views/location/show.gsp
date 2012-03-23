
<%@ page import="com.dtosolutions.Location" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'location.label', default: 'Location')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-location" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list location">
			
				<g:if test="${locationInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="location.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${locationInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.providerName}">
				<li class="fieldcontain">
					<span id="providerName-label" class="property-label"><g:message code="location.providerName.label" default="Provider Name" /></span>
					
						<span class="property-value" aria-labelledby="providerName-label"><g:fieldValue bean="${locationInstance}" field="providerName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.city}">
				<li class="fieldcontain">
					<span id="city-label" class="property-label"><g:message code="location.city.label" default="City" /></span>
					
						<span class="property-value" aria-labelledby="city-label"><g:fieldValue bean="${locationInstance}" field="city"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.province}">
				<li class="fieldcontain">
					<span id="province-label" class="property-label"><g:message code="location.province.label" default="Province" /></span>
					
						<span class="property-value" aria-labelledby="province-label"><g:fieldValue bean="${locationInstance}" field="province"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.country}">
				<li class="fieldcontain">
					<span id="country-label" class="property-label"><g:message code="location.country.label" default="Country" /></span>
					
						<span class="property-value" aria-labelledby="country-label"><g:fieldValue bean="${locationInstance}" field="country"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.postalCode}">
				<li class="fieldcontain">
					<span id="postalCode-label" class="property-label"><g:message code="location.postalCode.label" default="Postal Code" /></span>
					
						<span class="property-value" aria-labelledby="postalCode-label"><g:fieldValue bean="${locationInstance}" field="postalCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="location.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${locationInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="location.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${locationInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${locationInstance?.nodes}">
				<li class="fieldcontain">
					<span id="nodes-label" class="property-label"><g:message code="location.nodes.label" default="Nodes" /></span>
					
						<g:each in="${locationInstance.nodes}" var="n">
						<span class="property-value" aria-labelledby="nodes-label"><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${locationInstance?.id}" />
					<g:link class="edit" action="edit" id="${locationInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
