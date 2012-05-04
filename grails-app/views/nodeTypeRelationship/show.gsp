
<%@ page import="com.dtosolutions.NodeTypeRelationship" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeTypeRelationship.label', default: 'NodeTypeRelationship')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-nodeTypeRelationship" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			
		<table width="100%" border="0" cellspacing=0 cellpadding=0 valign=top>
			<tr>
				<td valign=top>
					<div style="clear: left;">
					<span class="property-value" aria-labelledby="roleName-label">${nodeTypeRelationshipInstance.roleName}</span>
					<table class="scaffold" width="450" border="0" cellspacing=5>
						<tr style="background-color:#021faf;">
							<td>
							<table class="scaffold" border="0" cellspacing=5>
								<tr>
									<td><img src="${resource(dir:path,file:nodeTypeRelationshipInstance?.parent.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" /></td>
									<td style="padding-left:5px;"><b>Parent:</b><g:link controller="nodeType" action="show" id="${nodeTypeRelationshipInstance?.parent?.id}" style="font: bold 17px verdana, arial, helvetica, sans-serif">${nodeTypeRelationshipInstance?.parent?.encodeAsHTML()}</g:link></td>
								</tr>
								<tr>
									<td><img src="${resource(dir:path,file:nodeTypeRelationshipInstance?.child.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" /></td>
									<td style="padding-left:5px;"><b>Child:</b><g:link controller="nodeType" action="show" id="${nodeTypeRelationshipInstance?.child?.id}" style="font: bold 17px verdana, arial, helvetica, sans-serif">${nodeTypeRelationshipInstance?.child?.encodeAsHTML()}</g:link></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
						<tr>
							<td>
								<table width=100%>
										<tr>
											<td>Role Name</td>
											<td>${nodeTypeRelationshipInstance.roleName}</td>
										</tr>
										<tr>
											<td>Parent Cardinality</td>
											<td>${nodeTypeRelationshipInstance.parentCardinality}</td>
										</tr>
										<tr>
											<td>Child Cardinality</td>
											<td>${nodeTypeRelationshipInstance.childCardinality}</td>
										</tr>
								</table>
			
							</td>
						</tr>
						<tr>
							<td>
								<g:form>
									<fieldset class="form_footer">
										<g:hiddenField name="id" value="${nodeTypeRelationshipInstance?.id}" />
										<span class="fake_button"><g:link action="edit" id="${nodeTypeRelationshipInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>
										<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
									</fieldset>
								</g:form>
							</td>
						</tr>
					</table>
					</div>
				</td>
				<td valign=top width=225>&nbsp;</td>
			</tr>
		</table>
		</div>
	</body>
</html>
