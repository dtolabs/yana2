<%@ page import="com.dtosolutions.Webhook" %>



<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'url', 'error')} required">
	<label for="url">
		<g:message code="webhook.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="url" value="${webhookInstance?.url}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'name', 'error')} ">
	<label for="url">
		<g:message code="webhook.name.label" default="Name" />
	</label>
	<g:textField name="name" value="${webhookInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'service', 'error')} required">
	<label for="service">
		<g:message code="webhook.service.label" default="Service" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="service" from="${service}" value="${webhookInstance?.service}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'format', 'error')} required">
	<label for="format">
		<g:message code="webhook.format.label" default="Format" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="format" from="${format}" value="${webhookInstance?.format}"/>
</div>