<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Import/Overwrite</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${attributesInstance}">
            <div class="errors">
                <g:renderErrors bean="${attributesInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:uploadForm action="overwrite">
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">Import XML Source File:</td>
                                <td valign="top" class="value"><input type="file" name="myFile" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="import" class="save" value="import/overwrite" /></span>
                </div>
            </g:uploadForm>
        </div>
    </body>
</html>
