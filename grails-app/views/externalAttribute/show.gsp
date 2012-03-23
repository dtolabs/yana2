

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'externalAttribute.label', default: 'ExternalAttribute')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="externalAttribute.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: externalAttributeInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="externalAttribute.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: externalAttributeInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="externalAttribute.value.label" default="Value" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: externalAttributeInstance, field: "value")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="externalAttribute.dataType.label" default="Data Type" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: externalAttributeInstance, field: "dataType")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="externalAttribute.attributes.label" default="Attributes" /></td>
                            
                            <td valign="top" class="value"><g:link controller="attributes" action="show" id="${externalAttributeInstance?.attributes?.id}">${externalAttributeInstance?.attributes?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${externalAttributeInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
