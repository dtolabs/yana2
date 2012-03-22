<%@ page import="com.dtosolutions.Artifact" %>



<div class="fieldcontain ${hasErrors(bean: artifactInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="artifact.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${artifactInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: artifactInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="artifact.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${artifactInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: artifactInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="artifact.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${artifactInstance?.dateModified}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: artifactInstance, field: 'instances', 'error')} ">
	<label for="instances">
		<g:message code="artifact.instances.label" default="Instances" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${artifactInstance?.instances?}" var="i">
    <li><g:link controller="instance" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="instance" action="create" params="['artifact.id': artifactInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'instance.label', default: 'Instance')])}</g:link>
</li>
</ul>

</div>

