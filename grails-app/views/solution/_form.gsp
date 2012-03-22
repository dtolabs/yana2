<%@ page import="com.dtosolutions.Solution" %>



<div class="fieldcontain ${hasErrors(bean: solutionInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="solution.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${solutionInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: solutionInstance, field: 'parent', 'error')} ">
	<label for="parent">
		<g:message code="solution.parent.label" default="Parent" />
		
	</label>
	<g:select id="parent" name="parent.id" from="${com.dtosolutions.Solution.list()}" optionKey="id" value="${solutionInstance?.parent?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: solutionInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="solution.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${solutionInstance?.dateModified}"  />
</div>

