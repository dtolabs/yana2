<%@ page import="com.dtolabs.NodeTypeRelationship" %>



<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'roleName', 'error')} required">
	<label for="roleName">
		<g:message code="nodeTypeRelationship.roleName.label" default="Role Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="roleName" value="${(params.roleName)?params.roleName:nodeTypeRelationshipInstance?.roleName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'parentCardinality', 'error')} ">
	<label for="parentCardinality">
		<g:message code="nodeTypeRelationship.parentCardinality.label" default="Parent Cardinality" />
		
	</label>
	<span class="styled-select"><g:select name="parentCardinality" from="${cardinality.entrySet()}" optionKey="key" optionValue="value" value="${(params.parentCardinality)?params.parentCardinality:nodeTypeRelationshipInstance.parentCardinality}"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: nodeTypeRelationshipInstance, field: 'childCardinality', 'error')} ">
	<label for="childCardinality">
		<g:message code="nodeTypeRelationship.childCardinality.label" default="Child Cardinality" />
		
	</label>
	<span class="styled-select"><g:select name="childCardinality" from="${cardinality.entrySet()}" optionKey="key" optionValue="value" value="${(params.childCardinality)?params.childCardinality:nodeTypeRelationshipInstance.childCardinality}"/></span>
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

