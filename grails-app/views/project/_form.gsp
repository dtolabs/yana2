<%@ page import="com.dtolabs.Project" %>



<div class="fieldcontain ${hasErrors(bean: project, field: 'name', 'error')} required">
    <label for="name">
        <g:message code="project.name.label" default="Name"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="name" required="" value="${project?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: project, field: 'description', 'error')} required">
    <label for="description">
        <g:message code="project.description.label" default="Description"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textArea name="description" rows="2" cols="50">${project?.description}</g:textArea>
</div>
