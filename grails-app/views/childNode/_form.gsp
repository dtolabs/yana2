<%@ page import="com.dtosolutions.ChildNode" %>



<div class="fieldcontain ${hasErrors(bean: childNodeInstance, field: 'parent', 'error')} required">
	<label for="parent">
		<g:message code="childNode.parent.label" default="Parent" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="parent" name="parent" from="${com.dtosolutions.Node.list()}" optionKey="id" required="" value="${parent.parent?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: childNodeInstance, field: 'child', 'error')} required">
	<label for="child">
		<g:message code="childNode.child.label" default="Child" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="child" name="child" from="${com.dtosolutions.Node.list()}" optionKey="id" required="" value="${child?.child?.id}" class="many-to-one"/>
</div>

