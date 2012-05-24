
<%@ page import="com.dtosolutions.ChildNode" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'childNode.label', default: 'ChildNode')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-childNode" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			
		<table width="100%" border="0" cellspacing=0 cellpadding=0 valign=top>
			<tr>
				<td valign=top>
					<div style="clear: left;">

					<table class="scaffold" width="600" border="0" cellspacing=5>
						<tr style="background-color:#021faf;">
							<td style="padding:10px;">
							<img src="${resource(dir:'images/icons/64',file:'Node.png')}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" />
							<span class="image-title">${childNodeInstance.relationshipName}</span>
							<br clear=left>
							
<div style="padding-top:5px;">
	<label for="id">
		<b>ID:</b>
	</label>
	<g:fieldValue bean="${childNodeInstance}" field="id"/>
</div>
							
							</td>
						</tr>
						<tr>
							<td>
							
							<table border="0" cellspacing=5>
								<tr>
									<td><b>Parent:</b></td>
									<td><img src="${resource(dir:path,file:childNodeInstance?.parent?.nodetype.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" /></td>
									<td><g:link controller="node" action="show" id="${childNodeInstance?.parent?.id}" style="font: bold 13px verdana, arial, helvetica, sans-serif">${childNodeInstance?.parent?.encodeAsHTML()}</g:link> [<g:link controller="nodeType" action="show" id="${childNodeInstance?.parent?.nodetype.id}" style="font: bold 13px verdana, arial, helvetica, sans-serif">${childNodeInstance?.parent?.nodetype.name.encodeAsHTML()}</g:link>]</td>
								</tr>
								<tr>
									<td><b>Child:</b></td>
									<td><img src="${resource(dir:path,file:childNodeInstance?.child?.nodetype.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" /></td>
									<td><g:link controller="node" action="show" id="${childNodeInstance?.child?.id}" style="font: bold 13px verdana, arial, helvetica, sans-serif">${childNodeInstance?.child?.encodeAsHTML()}</g:link> [<g:link controller="nodeType" action="show" id="${childNodeInstance?.child?.nodetype.id}" style="font: bold 13px verdana, arial, helvetica, sans-serif">${childNodeInstance?.child?.nodetype.name.encodeAsHTML()}</g:link>]</td>
								</tr>
							</table>

							</td>
						</tr>
						<tr>
							<td>
								<g:form>
									<fieldset class="form_footer">
										<g:hiddenField name="id" value="${childNodeInstance?.id}" />
										<span class="fake_button"><g:link action="edit" id="${childNodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>
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
