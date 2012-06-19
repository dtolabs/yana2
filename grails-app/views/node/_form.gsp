<%@ page import="com.dtolabs.Node" %>



<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="node.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${nodeInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="node.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${nodeInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'template', 'error')} required">
	<label for="template">
		<g:message code="node.template.label" default="Template" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="template" name="template.id" from="${com.dtosolutions.Template.list()}" optionKey="id" required="" value="${nodeInstance?.template?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="node.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="status" from="${com.dtolabs.Status?.values()}" keys="${com.dtolabs.Status.values()*.name()}" required="" value="${nodeInstance?.status?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'tags', 'error')} ">
	<label for="tags">
		<g:message code="node.tags.label" default="Tags" />
		
	</label>
	<g:textField name="tags" value="${nodeInstance?.tags}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'nodetype', 'error')} required">
	<label for="nodetype">
		<g:message code="node.nodetype.label" default="Nodetype" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="nodetype" name="nodetype.id" from="${com.dtolabs.NodeType.list()}" optionKey="id" required="" value="${nodeInstance?.nodetype?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'parent', 'error')} ">
	<label for="parent">
		<g:message code="node.parent.label" default="Parent" />
		
	</label>
	<g:select id="parent" name="parent.id" from="${com.dtolabs.Node.list()}" optionKey="id" value="${nodeInstance?.parent?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="node.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${nodeInstance?.dateModified}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'instances', 'error')} ">
	<label for="instances">
		<g:message code="node.instances.label" default="Instances" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${nodeInstance?.instances?}" var="i">
    <li><g:link controller="instance" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="instance" action="create" params="['node.id': nodeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'instance.label', default: 'Instance')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'nodes', 'error')} ">
	<label for="nodes">
		<g:message code="node.nodes.label" default="Nodes" />
	</label>
	
<ul class="one-to-many">
<g:each in="${nodeInstance?.nodes?}" var="n">
    <li><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="node" action="create" params="['node.id': nodeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'node.label', default: 'Node')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'nodeValues', 'error')} ">
	<label for="nodeValues">
		<g:message code="node.nodeValues.label" default="Template Values" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${nodeInstance?.nodeValues?}" var="t">
    <li><g:link controller="nodeValue" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="nodeValue" action="create" params="['node.id': nodeInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'nodeValue.label', default: 'NodeValue')])}</g:link>
</li>
</ul>

</div>

