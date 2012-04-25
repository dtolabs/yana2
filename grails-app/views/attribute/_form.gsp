<%@ page import="com.dtosolutions.Attribute" %>



<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="attribute.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${attributeInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: attributeInstance, field: 'filter', 'error')} required">
	<label for="filter">
		<g:message code="attribute.filter.label" default="Filter" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="filter" name="filter.id" from="${com.dtosolutions.Filter.list()}" optionKey="id" required="" value="${attributeInstance?.filter?.id}" class="many-to-one"/>
</div>

