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
	<g:select id="template" name="template.id" from="${com.dtosolutions.Template.list()}" optionKey="id" required="" value="${templateAttributeInstance?.template?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateAttributeInstance, field: 'required', 'error')} ">
	<label for="required">
		<g:message code="templateAttribute.required.label" default="Required" />
		
	</label>
	<g:checkBox name="required" value="${templateAttributeInstance?.required}" />
</div>

<div class="fieldcontain ${hasErrors(bean: templateAttributeInstance, field: 'values', 'error')} ">
	<label for="values">
		<g:message code="templateAttribute.values.label" default="Values" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${templateAttributeInstance?.values?}" var="v">
    <li><g:link controller="templateValue" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="templateValue" action="create" params="['templateAttribute.id': templateAttributeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'templateValue.label', default: 'TemplateValue')])}</g:link>
</li>
</ul>

</div>

