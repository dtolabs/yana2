<%@ page import="com.dtosolutions.Webhook" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'webhook.label', default: 'Webhook')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="create-webhook" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${webhookInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${webhookInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form controller="node" action="webhook" >
				<g:hiddenField name="service" value="node" />
				<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'url', 'error')} ">
					<label for="url">
						<g:message code="webhook.url.label" default="Url" />
						
					</label>
					<g:textField name="url" value="${webhookInstance?.url}"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'name', 'error')} ">
					<label for="url">
						<g:message code="webhook.name.label" default="Name" />
						
					</label>
					<g:textField name="name" value="${webhookInstance?.name}"/>
				</div>
				<fieldset class="form_footer">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
