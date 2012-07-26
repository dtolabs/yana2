<%@ page import="com.dtolabs.NodeType" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>		
	</head>
	<body>

		<div id="create-nodeType" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:form action="save" >
				<table border=0 cellspacing=0 cellpadding=0 valign=top>
					<tr valign=top>
						<td>Name:</td><td><g:textField name="name" required="" value="${nodeTypeInstance?.name}"/></td>
					</tr>			
					<tr valign=top>
						<td>Description:</td><td><g:textField name="description" value="${nodeTypeInstance?.description}"/></td>
					</tr>
					<tr valign=top>
						<td>Image:</td><td><g:select name="image" from="${images}" value="${nodeTypeInstance?.image}"/></td>

					</tr>

				</table>

				
				<fieldset class="form_footer">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>

			</g:form>
		</div>
	</body>
</html>
