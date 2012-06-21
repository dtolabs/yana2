<%@ page import="com.dtolabs.NodeAttribute" %>



<div class="fieldcontain ${hasErrors(bean: nodeAttributeInstance, field: 'attribute', 'error')} required">
	<label for="attribute">
		<g:message code="nodeAttribute.attribute.label" default="Attribute" />
		<span class="required-indicator">*</span>
	</label>
	<span class="styled-select"><g:select id="attribute" name="attribute.id" from="${com.dtolabs.Attribute.list()}" optionKey="id" required="" value="${nodeAttributeInstance?.attribute?.id}" class="many-to-one"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeAttributeInstance, field: 'template', 'error')} required">
	<label for="template">
		<g:message code="nodeAttribute.template.label" default="Template" />
		<span class="required-indicator">*</span>
	</label>
	<span class="styled-select"><g:select id="template" name="template.id" from="${com.dtolabs.NodeType.list()}" optionKey="id" required="" value="${nodeAttributeInstance?.template?.id}" class="many-to-one"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeAttributeInstance, field: 'required', 'error')} ">
	<label for="required">
		<g:message code="nodeAttribute.required.label" default="Required" />
		
	</label>
	<g:checkBox name="required" value="${nodeAttributeInstance?.required}" />
</div>

