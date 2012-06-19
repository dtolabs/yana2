<%@ page import="com.dtolabs.NodeValue" %>



<div class="fieldcontain ${hasErrors(bean: nodeValueInstance, field: 'node', 'error')} required">
	<label for="node">
		<g:message code="nodeValue.node.label" default="Node" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="node" name="node.id" from="${com.dtolabs.Node.list()}" optionKey="id" required="" value="${nodeValueInstance?.node?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeValueInstance, field: 'templateattribute', 'error')} required">
	<label for="templateattribute">
		<g:message code="nodeValue.templateattribute.label" default="Templateattribute" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="templateattribute" name="templateattribute.id" from="${com.dtolabs.NodeAttribute.list()}" optionKey="id" required="" value="${nodeValueInstance?.templateattribute?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeValueInstance, field: 'value', 'error')} ">
	<label for="value">
		<g:message code="nodeValue.value.label" default="Value" />
		
	</label>
	<g:textField name="value" value="${nodeValueInstance?.value}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeValueInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="nodeValue.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${nodeValueInstance?.dateModified}"  />
</div>

