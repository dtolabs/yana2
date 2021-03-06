<%@ page import="com.dtolabs.Attribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'attribute.label', default: 'Attribute')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

	<div id="show-attribute" class="content scaffold-show" role="main">
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
							<img src="${resource(dir:'images/icons/64',file:'Attribute.png')}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" />
							<span class="image-title">${attributeInstance.name}</span>
							<br clear=left>

<div style="padding-top:5px;">
	<label for="id">
		<b>ID:</b>
	</label>
	<g:fieldValue bean="${attributeInstance}" field="id"/>
</div>
							</td>
						</tr>
						<tr>
							<td>
								<table width=375 align=left>
									<thead>
										<tr>
											<td width=150><span class="property-value" aria-labelledby="name-label"><b>Filter</b></td>
											<td align=left><span class="property-value" aria-labelledby="filter-label"><g:link controller="filter" action="show" id="${attributeInstance?.filter?.id}">${attributeInstance?.filter?.encodeAsHTML()}</g:link></span></td>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table width=375>
									<thead>
										<tr>
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="attribute.dateCreated.label" default="Date Created" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${attributeInstance?.dateCreated}" /></td>
										</tr>
									
										<tr>
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="attribute.lastUpdated.label" default="Date Modified" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${attributeInstance?.lastUpdated}" /></td>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<g:form>
									<fieldset class="form_footer">
										<g:hiddenField name="id" value="${attributeInstance?.id}" />
										<!-- <span class="fake_button"><g:link action="edit" id="${attributeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>-->
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
