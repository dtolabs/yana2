<%@ page import="com.dtolabs.NodeValue" %>



<div class="fieldcontain ${hasErrors(bean: templateValueInstance, field: 'node', 'error')} required">
	<label for="node">
		<g:message code="templateValue.node.label" default="Node" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="node" name="node.id" from="${com.dtolabs.Node.list()}" optionKey="id" required="" value="${templateValueInstance?.node?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateValueInstance, field: 'templateattribute', 'error')} required">
	<label for="templateattribute">
		<g:message code="templateValue.templateattribute.label" default="Templateattribute" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="templateattribute" name="templateattribute.id" from="${com.dtolabs.NodeAttribute.list()}" optionKey="id" required="" value="${templateValueInstance?.templateattribute?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateValueInstance, field: 'value', 'error')} ">
	<label for="value">
		<g:message code="templateValue.value.label" default="Value" />
		
	</label>
	<g:textField name="value" value="${templateValueInstance?.value}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: templateValueInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="templateValue.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${templateValueInstance?.dateModified}"  />
</div>

