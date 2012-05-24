<%@ page import="com.dtosolutions.ChildNode" %>



<div class="fieldcontain ${hasErrors(bean: childNodeInstance, field: 'relationshipName', 'error')}  required">
	<label for="relationshipName">
		<g:message code="childNode.relationshipName.label" default="Relationship Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="relationshipName" value="${childNodeInstance?.relationshipName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: childNodeInstance, field: 'parent', 'error')} required">
	<label for="parent">
		<g:message code="childNode.parent.label" default="Parent" />
		<span class="required-indicator">*</span>
	</label>
	<span class="styled-select"><g:select id="parent" name="parent.id" from="${com.dtosolutions.Node.list()}" optionKey="id" required="" value="${childNodeInstance?.parent?.id}" class="many-to-one"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: childNodeInstance, field: 'child', 'error')} required">
	<label for="child">
		<g:message code="childNode.child.label" default="Child" />
		<span class="required-indicator">*</span>
	</label>
	<span class="styled-select"><g:select id="child" name="child.id" from="${com.dtosolutions.Node.list()}" optionKey="id" required="" value="${childNodeInstance?.child?.id}" class="many-to-one"/></span>
</div>

