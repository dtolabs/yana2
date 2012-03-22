
<%@ page import="com.dtosolutions.Artifact" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'artifact.label', default: 'Artifact')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-artifact" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-artifact" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list artifact">
			
				<g:if test="${artifactInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="artifact.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${artifactInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${artifactInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="artifact.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${artifactInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${artifactInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="artifact.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${artifactInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${artifactInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="artifact.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${artifactInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${artifactInstance?.instances}">
				<li class="fieldcontain">
					<span id="instances-label" class="property-label"><g:message code="artifact.instances.label" default="Instances" /></span>
					
						<g:each in="${artifactInstance.instances}" var="i">
						<span class="property-value" aria-labelledby="instances-label"><g:link controller="instance" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${artifactInstance?.id}" />
					<g:link class="edit" action="edit" id="${artifactInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
