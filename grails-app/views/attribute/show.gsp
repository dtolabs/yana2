
<%@ page import="com.dtosolutions.Attribute" %>
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

					<table class="scaffold" width="100%" border="0" cellspacing=5>
						<tr>
							<td>
								<table width="375" cellpadding=5 style="border: 1px solid #0431f7;">
									<tr>
										<td><span id="name-label" class="property-label"><g:message code="attribute.name.label" default="Name" /></span></td>
										<td><span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${attributeInstance}" field="name"/></span></td>
									</tr>
									<tr>
										<td><span id="filter-label" class="property-label"><g:message code="attribute.filter.label" default="Filter" /></span></td>
										<td><span class="property-value" aria-labelledby="filter-label"><g:link controller="filter" action="show" id="${attributeInstance?.filter?.id}">${attributeInstance?.filter?.encodeAsHTML()}</g:link></span></td>
									</tr>
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
			</tr>
		</table>

	</div>
	</body>
</html>
