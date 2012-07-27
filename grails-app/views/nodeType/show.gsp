
<%@ page import="com.dtolabs.NodeType" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

	<div id="show-nodeType" class="content scaffold-show" role="main">
		<table width="100%" border="0" cellspacing=0 cellpadding=0 valign=top>
			<tr>
				<td valign=top><h1><g:message code="default.show.label" args="[entityName]" /></h1></td>
				<td width=75>
					<g:form controller="node" action="create">
							<g:hiddenField name="nodetype" value="${nodeTypeInstance?.id}" />
							<g:submitButton name="create" value="Create Node" />
					</g:form>
				</td>
				<td width=175>
					&nbsp;<span class="fake_button"><a href="/search/index?q=nodetype:${nodeTypeInstance.name}">Find Nodes</a></span>
				</td>
			</tr>
		</table>
				
		<g:if test="${flash.message}">
		<div class="message" role="status">${flash.message}</div>
		</g:if>
		

		<table width="100%" border="0" cellspacing=0 cellpadding=0 valign=top>
			<tr>
				<td valign=top>
					<div style="clear: left;">
					<table class="scaffold" width="450" border="0" cellspacing=5>
						<g:if test="${nodeTypeInstance.image}">
						<tr style="background-color:#021faf;">
							<td style="padding:10px;">
							<img src="${resource(dir:path,file:nodeTypeInstance.image)}" alt="" style="padding: 0px 25px 0px 7px;vertical-align:middle;" align="left" />
							<span class="image-title"><g:fieldValue bean="${nodeTypeInstance}" field="name"/></span>
							<g:if test="${nodeTypeInstance?.description}"><div class="image-description"><g:fieldValue bean="${nodeTypeInstance}" field="description"/></div></g:if><br clear=left>
							
<div style="padding-top:5px;">
	<label for="id">
		<b>ID:</b>
	</label>
	<g:fieldValue bean="${nodeTypeInstance}" field="id"/>
</div>
							
							</td>
						</tr>
						</g:if>
						<tr>
							<td>
			
								<table width=250>
									<tbody>
									
									<g:if test="${nodeTypeInstance?.attributes}">
									<tr>
										<g:each in="${com.dtolabs.NodeAttribute.findAllByNodetype(com.dtolabs.NodeType.get(nodeTypeInstance?.id), [sort:'attribute.name',order:'asc'])}" status="i" var="t">
										<g:set var="attributeInstance" value="${com.dtolabs.Attribute.findAllById(t?.attribute?.id, [sort:'name',order:'asc'])}" />
										
										<tr>
											<td><g:link controller="attribute" action="show" id="${attributeInstance.id[0]}"><b>${attributeInstance.name[0]} <g:if test="${t.required}">*</g:if></b></g:link></td>
											<td><g:link controller="filter" action="show" id="${attributeInstance.filter.id[0]}">${attributeInstance.filter.dataType[0]}</g:link></td>
										</tr>
										</g:each>
									</g:if>
									</tbody>
								</table>
			
						
							</td>
						</tr>
						<tr>
							<td>
								<h4>* = required field</h4>
								<table width=375>
									<thead>
										
										<tr>
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="nodeType.dateCreated.label" default="Date Created" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${nodeTypeInstance?.dateCreated}" /></td>
										</tr>
									
										<tr>
											<td style="font: bold 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:message code="nodeType.dateModified.label" default="Date Modified" />: </td>
											<td style="font: 11px verdana, arial, helvetica, sans-serif;color:#0431f7;"><g:formatDate date="${nodeTypeInstance?.dateModified}" /></td>
										</tr>
									</thead>
								</table>
							</td>
						</tr>
					</table>
					</div>
				</td>
				<td valign=top>
					<h3 style="padding:0;margin:0;">Relationships</h3>
					<table width="225" cellspacing=5 style="border: 1px solid #0431f7;">
						<tr>
							<td><h3 style="padding:0;margin:0;">Parents</h3>
								<ul>
								<g:if test="${parents}">
								<g:each in="${parents}" status="i" var="parent">
									<li class="fieldcontain">
										<span class="property-value" aria-labelledby="filter-label">
										<g:link controller="nodeTypeRelationship" action="show" id="${parent?.id}">${parent?.parent?.name?.encodeAsHTML()}</g:link>
										</span>
									</li>
								</g:each>
								</g:if>
								</ul>
							</td>
						</tr>
						<tr>
							<td><h3 style="padding:0;margin:0;">Children</h3>
								<ul>
								<g:if test="${children}">
								<g:each in="${children}" status="i" var="child">
									<li class="fieldcontain">
									<span class="property-value" aria-labelledby="filter-label">
									<g:link controller="nodeTypeRelationship" action="show" id="${child?.id}">${child?.child?.name?.encodeAsHTML()} 
									</g:link>
									</span>
									</li>
								</g:each>
								</g:if>
								</ul>
							</td>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<g:form>
						<fieldset class="form_footer">
							<g:hiddenField name="id" value="${nodeTypeInstance?.id}" />
							<!--<span class="fake_button"><g:link action="edit" id="${nodeTypeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link></span>-->
							<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>
