<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Import</title>
    </head>
    <body>
        <div class="body">
            <h1>XML Import Successful</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${attributesInstance}">
            <div class="errors">
                <g:renderErrors bean="${attributesInstance}" as="list" />
            </div>
            </g:hasErrors>
            Your import has successfully finished!
        </div>
    </body>
</html>
