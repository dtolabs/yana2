
<%@ page import="com.dtolabs.NodeValue" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeValue.label', default: 'NodeValue')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-nodeValue" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-nodeValue" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list nodeValue">
			
				<g:if test="${nodeValueInstance?.node}">
				<li class="fieldcontain">
					<span id="node-label" class="property-label"><g:message code="nodeValue.node.label" default="Node" /></span>
					
						<span class="property-value" aria-labelledby="node-label"><g:link controller="node" action="show" id="${nodeValueInstance?.node?.id}">${nodeValueInstance?.node?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeValueInstance?.nodeattribute}">
				<li class="fieldcontain">
					<span id="nodeattribute-label" class="property-label"><g:message code="nodeValue.nodeattribute.label" default="Nodeattribute" /></span>
					
						<span class="property-value" aria-labelledby="nodeattribute-label"><g:link controller="nodeAttribute" action="show" id="${nodeValueInstance?.nodeattribute?.id}">${nodeValueInstance?.nodeattribute?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeValueInstance?.value}">
				<li class="fieldcontain">
					<span id="value-label" class="property-label"><g:message code="nodeValue.value.label" default="Value" /></span>
					
						<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${nodeValueInstance}" field="value"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeValueInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="nodeValue.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${nodeValueInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${nodeValueInstance?.dateModified}">
				<li class="fieldcontain">
					<span id="dateModified-label" class="property-label"><g:message code="nodeValue.dateModified.label" default="Date Modified" /></span>
					
						<span class="property-value" aria-labelledby="dateModified-label"><g:formatDate date="${nodeValueInstance?.dateModified}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${nodeValueInstance?.id}" />
					<g:link class="edit" action="edit" id="${nodeValueInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
