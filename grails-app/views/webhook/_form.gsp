<%@ page import="com.dtosolutions.Webhook" %>



<div class="fieldcontain ${hasErrors(bean: webhookInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="webhook.url.label" default="Url" />
		
	</label>
	<g:textField name="url" value="${webhookInstance?.url}"/>
</div>

