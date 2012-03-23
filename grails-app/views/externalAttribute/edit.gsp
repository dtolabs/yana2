
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'externalAttribute.label', default: 'ExternalAttribute')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${externalAttributeInstance}">
            <div class="errors">
                <g:renderErrors bean="${externalAttributeInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${externalAttributeInstance?.id}" />
                <g:hiddenField name="version" value="${externalAttributeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="externalAttribute.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: externalAttributeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${externalAttributeInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="value"><g:message code="externalAttribute.value.label" default="Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: externalAttributeInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${externalAttributeInstance?.value}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dataType"><g:message code="externalAttribute.dataType.label" default="Data Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: externalAttributeInstance, field: 'dataType', 'errors')}">
                                    <g:select name="dataType" from="${externalAttributeInstance.constraints.dataType.inList}" value="${externalAttributeInstance?.dataType}" valueMessagePrefix="externalAttribute.dataType"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="attributes"><g:message code="externalAttribute.attributes.label" default="Attributes" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: externalAttributeInstance, field: 'attributes', 'errors')}">
                                    <g:select name="attributes.id" from="${yana.attributes.Attributes.list()}" optionKey="id" value="${externalAttributeInstance?.attributes?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
