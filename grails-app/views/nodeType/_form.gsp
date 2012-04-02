<%@ page import="com.dtosolutions.NodeType" %>



<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="nodeType.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${nodeTypeInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="nodeType.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${nodeTypeInstance?.dateModified}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'nodes', 'error')} ">
	<label for="nodes">
		<g:message code="nodeType.nodes.label" default="Nodes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${nodeTypeInstance?.nodes?}" var="n">
    <li><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="node" action="create" params="['nodeType.id': nodeTypeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'node.label', default: 'Node')])}</g:link>
</li>
</ul>

</div>

