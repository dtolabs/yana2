
<%@ page import="com.dtosolutions.Location" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'location.label', default: 'Location')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-location" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-location" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'location.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="providerName" title="${message(code: 'location.providerName.label', default: 'Provider Name')}" />
					
						<g:sortableColumn property="city" title="${message(code: 'location.city.label', default: 'City')}" />
					
						<g:sortableColumn property="province" title="${message(code: 'location.province.label', default: 'Province')}" />
					
						<g:sortableColumn property="country" title="${message(code: 'location.country.label', default: 'Country')}" />
					
						<g:sortableColumn property="postalCode" title="${message(code: 'location.postalCode.label', default: 'Postal Code')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${locationInstanceList}" status="i" var="locationInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${locationInstance.id}">${fieldValue(bean: locationInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: locationInstance, field: "providerName")}</td>
					
						<td>${fieldValue(bean: locationInstance, field: "city")}</td>
					
						<td>${fieldValue(bean: locationInstance, field: "province")}</td>
					
						<td>${fieldValue(bean: locationInstance, field: "country")}</td>
					
						<td>${fieldValue(bean: locationInstance, field: "postalCode")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${locationInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
