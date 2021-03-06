<%@ page import="com.dtolabs.NodeType" %>



<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="nodeType.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${nodeTypeInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="nodeType.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${nodeTypeInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'image', 'error')} ">
	<label for="image">
		<g:message code="nodeType.image.label" default="Image" />
	</label>
	<span class="styled-select"><g:select name="image" from="${images}" value="${nodeTypeInstance?.image}"/></span>
</div>
                            
<div class="fieldcontain ${hasErrors(bean: nodeTypeInstance, field: 'attributes', 'error')} ">
	<label for="attributes">
		<g:message code="nodeType.attributes.label" default="Attributes" />
		
	</label>
	
<ul class="one-to-many">

<g:each in="${nodeTypeInstance?.attributes?}" var="a">
	<g:set var="attributeInstance" value="${com.dtolabs.Attribute.get(a.attribute.id)}"/>
    <li><g:link controller="NodeAttribute" action="show" id="${a.id}">${attributeInstance.name} [${attributeInstance.filter.dataType}]</g:link></li>
</g:each>
</ul>

<g:link controller="nodeAttribute" action="create" params="['template.id': templateInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'nodeAttribute.label', default: 'NodeAttribute')])}</g:link>

</div>





