<%@ page import="com.dtosolutions.NodeTypeRelationship" %>



<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'roleName', 'error')} ">
	<label for="roleName">
		<g:message code="nodeTypeRelationship.roleName.label" default="Role Name" />
		
	</label>
	<g:textField name="roleName" value="${nodeTypeRelationshipInstance?.roleName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'parentCardinality', 'error')} ">
	<label for="parentCardinality">
		<g:message code="nodeTypeRelationship.parentCardinality.label" default="Parent Cardinality" />
		
	</label>
	<g:field type="number" name="parentCardinality" value="${fieldValue(bean: nodeTypeRelationshipInstance, field: 'parentCardinality')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'childCardinality', 'error')} ">
	<label for="childCardinality">
		<g:message code="nodeTypeRelationship.childCardinality.label" default="Child Cardinality" />
		
	</label>
	<g:field type="number" name="childCardinality" value="${fieldValue(bean: nodeTypeRelationshipInstance, field: 'childCardinality')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'parent', 'error')} required">
	<label for="parent">
		<g:message code="nodeTypeRelationship.parent.label" default="Parent" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="parent" name="parent.id" from="${com.dtosolutions.NodeType.list()}" optionKey="id" required="" value="${nodeTypeRelationshipInstance?.parent?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'child', 'error')} required">
	<label for="child">
		<g:message code="nodeTypeRelationship.child.label" default="Child" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="child" name="child.id" from="${com.dtosolutions.NodeType.list()}" optionKey="id" required="" value="${nodeTypeRelationshipInstance?.child?.id}" class="many-to-one"/>
</div>

