<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Import</title>
    </head>
    <body>

        <div class="body">
            <h1>Import XML</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${attributesInstance}">
            <div class="errors">
                <g:renderErrors bean="${attributesInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:uploadForm action="savexml">
                <div class="dialog">
                    <table>
                        <tbody>
                        <tr>
                            <td class="name">
                                Project:
                            </td>
                            <td class="value">
                                <g:select name="project" from="${projectList}" optionKey="name"
                                                      optionValue="name" value="${session.project}"/>
                            </td>
                        </tr>
                            <tr class="prop">
                                <td valign="top" class="name">Import XML Source File:</td>
                                <td valign="top" class="value"><input type="file" name="yanaimport"/></td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:submitButton name="import" class="button" value="import" />
                </div>
            </g:uploadForm>
        </div>
    </body>
</html>
