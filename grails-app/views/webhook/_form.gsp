<%@ page import="com.dtosolutions.Webhook" %>



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