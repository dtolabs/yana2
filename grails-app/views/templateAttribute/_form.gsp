<%@ page import="com.dtosolutions.TemplateAttribute" %>



<div class="fieldcontain ${hasErrors(bean: templateAttributeInstance, field: 'attribute', 'error')} required">
	<label for="attribute">
		<g:message code="templateAttribute.attribute.label" default="Attribute" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="attribute" name="attribute.id" from="${com.dtosolutions.Attribute.list()}" optionKey="id" required="" value="${templateAttributeInstance?.attribute?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateAttributeInstance, field: 'template', 'error')} required">
	<label for="template">
		<g:message code="templateAttribute.template.label" default="Template" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="template" name="template.id" from="${com.dtosolutions.NodeType.list()}" optionKey="id" required="" value="${templateAttributeInstance?.template?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateAttributeInstance, field: 'required', 'error')} ">
	<label for="required">
		<g:message code="templateAttribute.required.label" default="Required" />
		
	</label>
	<g:checkBox name="required" value="${templateAttributeInstance?.required}" />
</div>

