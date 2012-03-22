

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'externalAttribute.label', default: 'ExternalAttribute')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${externalAttributeInstance}">
            <div class="errors">
                <g:renderErrors bean="${externalAttributeInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
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
                                    <g:select name="attributes.id" from="${com.dtosolutions.Attributes.list()}" optionKey="id" value="${externalAttributeInstance?.attributes?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
