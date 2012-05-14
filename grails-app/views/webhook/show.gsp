
<%@ page import="com.dtosolutions.Webhook" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'webhook.label', default: 'Webhook')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

		<div id="show-webhook" class="content scaffold-show" role="main">
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
								<span class="image-title">${webhookInstance.name}</span>
								</td>
							</tr>
							<tr>
								<td>
									<table width=375 align=left>
										<thead>
											<tr>
												<td width=150><span class="property-value" aria-labelledby="name-label"><b>URL</b></td>
												<td align=left><span class="property-value" aria-labelledby="filter-label"><g:link controller="webhook" action="show" id="${webhookInstance?.id}">${webhookInstance?.url}</g:link></span></td>
											</tr>
											<tr>
												<td width=150><span class="property-value" aria-labelledby="name-label"><b>User</b></td>
												<td align=left><span class="property-value" aria-labelledby="filter-label">${webhookInstance?.user?.username}</span></td>
											</tr>
											<tr>
												<td width=150><span class="property-value" aria-labelledby="name-label"><b>Format</b></td>
												<td align=left><span class="property-value" aria-labelledby="filter-label">${webhookInstance?.format}</span></td>
											</tr>
											<tr>
												<td width=150><span class="property-value" aria-labelledby="name-label"><b>Service</b></td>
												<td align=left><span class="property-value" aria-labelledby="filter-label">${webhookInstance?.service}</span></td>
											</tr>
											<tr>
												<td width=150><span class="property-value" aria-labelledby="name-label"><b>Fails</b></td>
												<td align=left><span class="property-value" aria-labelledby="filter-label">${webhookInstance?.attempts}</span></td>
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
												<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="attribute.dateModified.label" default="Date Modified" />: </td>
												<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${attributeInstance?.dateModified}" /></td>
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
											<span class="fake_button"><g:link action="edit" id="${attributeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>
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
			
			
			
			<ol class="property-list webhook">
			
				<g:if test="${webhookInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="webhook.url.label" default="Url" /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${webhookInstance}" field="url"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="form_footer">
					<g:hiddenField name="id" value="${webhookInstance?.id}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
