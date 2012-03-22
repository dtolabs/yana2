<%@ page import="com.dtosolutions.Template" %>



<div class="fieldcontain ${hasErrors(bean: templateInstance, field: 'templateName', 'error')} required">
	<label for="templateName">
		<g:message code="template.templateName.label" default="Template Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="templateName" required="" value="${templateInstance?.templateName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateInstance, field: 'attributes', 'error')} ">
	<label for="attributes">
		<g:message code="template.attributes.label" default="Attributes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${templateInstance?.attributes?}" var="a">
    <li><g:link controller="templateAttribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="templateAttribute" action="create" params="['template.id': templateInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'templateAttribute.label', default: 'TemplateAttribute')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: templateInstance, field: 'nodetype', 'error')} required">
	<label for="nodetype">
		<g:message code="template.nodetype.label" default="Nodetype" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="nodetype" name="nodetype.id" from="${com.dtosolutions.NodeType.list()}" optionKey="id" required="" value="${templateInstance?.nodetype?.id}" class="many-to-one"/>
</div>

