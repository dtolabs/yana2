<%@ page import="com.dtolabs.NodeTypeRelationship" %>



<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'name', 'error')} required">
	<label for="roleName">
		<g:message code="nodeTypeRelationship.roleName.label" default="Role Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="roleName" value="${(params.roleName)?params.roleName:nodeTypeRelationshipInstance?.roleName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'parent', 'error')} required">
	<label for="parent">
		<g:message code="nodeTypeRelationship.parent.label" default="Parent" />
		<span class="required-indicator">*</span>
	</label>
	<span class="styled-select"><g:select id="parent" name="parent.id" from="${com.dtolabs.NodeType.list()}" optionKey="id" required="" value="${(params.parent)?params.parent.id:nodeTypeRelationshipInstance?.parent?.id}" class="many-to-one"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'child', 'error')} required">
	<label for="child">
		<g:message code="nodeTypeRelationship.child.label" default="Child" />
		<span class="required-indicator">*</span>
	</label>
	<span class="styled-select"><g:select id="child" name="child.id" from="${com.dtolabs.NodeType.list()}" optionKey="id" required="" value="${(params.child)?params.child.id:nodeTypeRelationshipInstance?.child?.id}" class="many-to-one"/></span>
</div>

