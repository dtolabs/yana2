
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'externalAttribute.label', default: 'ExternalAttribute')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'externalAttribute.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'externalAttribute.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="value" title="${message(code: 'externalAttribute.value.label', default: 'Value')}" />
                        
                            <g:sortableColumn property="dataType" title="${message(code: 'externalAttribute.dataType.label', default: 'Data Type')}" />
                        
                            <th><g:message code="externalAttribute.attributes.label" default="Attributes" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${externalAttributeInstanceList}" status="i" var="externalAttributeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${externalAttributeInstance.id}">${fieldValue(bean: externalAttributeInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: externalAttributeInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: externalAttributeInstance, field: "value")}</td>
                        
                            <td>${fieldValue(bean: externalAttributeInstance, field: "dataType")}</td>
                        
                            <td>${fieldValue(bean: externalAttributeInstance, field: "attributes")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${externalAttributeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
