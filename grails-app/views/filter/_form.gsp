<%@ page import="com.dtosolutions.Filter" %>



<div class="fieldcontain ${hasErrors(bean: filterInstance, field: 'dataType', 'error')} required">
	<label for="dataType">
		<g:message code="filter.dataType.label" default="Data Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="dataType" required="" value="${filterInstance?.dataType}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: filterInstance, field: 'regex', 'error')} ">
	<label for="regex">
		<g:message code="filter.regex.label" default="Regex" />
		
	</label>
	<g:textArea name="regex" cols="40" rows="5" maxlength="500" value="${filterInstance?.regex}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: filterInstance, field: 'dateModified', 'error')} required">
	<label for="dateModified">
		<g:message code="filter.dateModified.label" default="Date Modified" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateModified" precision="day"  value="${filterInstance?.dateModified}"  />
</div>

