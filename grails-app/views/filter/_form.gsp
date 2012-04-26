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
	<span class="styled-select"><g:textArea name="regex" cols="40" rows="5" maxlength="500" value="${filterInstance?.regex}"/></span>
</div>

