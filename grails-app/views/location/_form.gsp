<%@ page import="com.dtosolutions.Location" %>



<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="location.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${locationInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'providerName', 'error')} ">
	<label for="providerName">
		<g:message code="location.providerName.label" default="Provider Name" />
		
	</label>
	<g:textField name="providerName" value="${locationInstance?.providerName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'city', 'error')} ">
	<label for="city">
		<g:message code="location.city.label" default="City" />
		
	</label>
	<g:textField name="city" value="${locationInstance?.city}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'province', 'error')} ">
	<label for="province">
		<g:message code="location.province.label" default="Province" />
		
	</label>
	<g:textField name="province" value="${locationInstance?.province}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'country', 'error')} ">
	<label for="country">
		<g:message code="location.country.label" default="Country" />
		
	</label>
	<g:textField name="country" value="${locationInstance?.country}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'postalCode', 'error')} ">
	<label for="postalCode">
		<g:message code="location.postalCode.label" default="Postal Code" />
		
	</label>
	<g:textField name="postalCode" value="${locationInstance?.postalCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="location.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${locationInstance?.dateModified}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'nodes', 'error')} ">
	<label for="nodes">
		<g:message code="location.nodes.label" default="Nodes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${locationInstance?.nodes?}" var="n">
    <li><g:link controller="node" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="node" action="create" params="['location.id': locationInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'node.label', default: 'Node')])}</g:link>
</li>
</ul>

</div>

