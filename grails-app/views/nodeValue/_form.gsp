<%@ page import="com.dtolabs.NodeValue" %>



<div class="fieldcontain ${hasErrors(bean: nodeValueInstance, field: 'node', 'error')} required">
	<label for="node">
		<g:message code="nodeValue.node.label" default="Node" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="node" name="node.id" from="${com.dtolabs.Node.list()}" optionKey="id" required="" value="${nodeValueInstance?.node?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeValueInstance, field: 'nodeattribute', 'error')} required">
	<label for="nodeattribute">
		<g:message code="nodeValue.nodeattribute.label" default="Nodeattribute" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="nodeattribute" name="nodeattribute.id" from="${com.dtolabs.NodeAttribute.list()}" optionKey="id" required="" value="${nodeValueInstance?.nodeattribute?.id}" class="many-to-one"/>
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

