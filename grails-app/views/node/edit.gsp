<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'node.label', default: 'Node')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		
  	<script type="text/javascript">
  	window.onload = init;
  	function init() {
  		var nodetype = ("${nodeInstance?.nodetype?.id}") ? "${nodeInstance?.nodetype?.id}" : $("#nodetype").val();
  		if(nodetype){
			getAttributes();
  	  	}
  	}

  	function getFormFields(){
  		getAttributes();
  	}

  	
  	function getAttributes(){
  	  	var node = "${nodeInstance?.id}"
  		var template = ("${nodeInstance?.nodetype?.id}") ? "${nodeInstance?.nodetype?.id}" : $("#nodetype").val();
  	  	if(template!=null){
			$.ajaxSetup({contentType:"application/json"});
			$.getJSON("${request.contextPath}/node/getTemplateAttributes",{templateid:template,node:node,ajax:'true'},function(json){
				if(json){
					var div = document.getElementById("attributes");
					div.innerHTML = '';
					var table = document.createElement('table');
					table.style.width = '480px';
					table.style.border = '0px';
					for(var i=0;i<json.length;i++){
						var j = json[i];
						var row = document.createElement("tr");
						row.id='att'+j.tid+'_row'
						
						var cell1 = document.createElement("td");
						cell1.id='att'+j.tid+'_cell1'
						if(j.required){
							cell1.innerHTML = '<b>'+j.val+'</b> ['+j.datatype+'] *:';
						}else{
							cell1.innerHTML = '<b>'+j.val+'</b> ['+j.datatype+']:';
						}
						cell1.style.width = '200px';
						row.appendChild(cell1);

						var cell2 = document.createElement("td");
						cell2.id='att'+j.tid+'_cell2'
						
						// input hidden - attid_require
						var require = document.createElement('input');
						require.type='hidden';
						require.name='att'+j.tid+'_require';
						require.value = j.required;
						require.id='att'+j.tid+'_require';
						
						// input hidden - attid_filter
						var filter = document.createElement('input');
						filter.type='hidden';
						filter.name='att'+j.tid+'_filter';
						filter.value = j.filter;
						filter.id='att'+j.tid+'_filter';
						
						// input text - attid
						var input = document.createElement('input');
						input.type='text';
						input.name='att'+j.tid;
						input.id='att'+j.tid;
						input.value = j.key;
						input.size = 20;
						input.onblur =  function () {validate(this)};

						cell2.appendChild(require);
						cell2.appendChild(filter);
						cell2.appendChild(input);
						row.appendChild(cell2);
						table.appendChild(row);
					}
					div.appendChild(table);
				}
			});
			$("#attributes").show();
  	  	}else{
  	  		$("#attributes").hide();
  	  	}
  	}

  	function validate(input){
  	  	var reset_color = '#ffffff';
  	  	var bad_color = '#ff0000';
  	  	
  		var val = input.value;
  		var filter = document.getElementById(input.id+"_filter").value;
  		var required = document.getElementById(input.id+"_require").value;
  		
  		if(!val.match(filter)){
			alert("Field does not meet required values. Please try again.");
			if(required){
				$(input.id+'_row').css("background-color","red");
  			}
  	  	}else{
  	  	  	$(input.id+'_row').css("background-color","white");
  	  	}
  	}
	</script>
	</head>
	<body>
		<div id="edit-node" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${nodeInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${nodeInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${nodeInstance?.id}" />
				<g:hiddenField name="version" value="${nodeInstance?.version}" />
				<g:hiddenField name="nodetype" value="${nodeInstance?.nodetype?.id}" />

			<table class="scaffold" border="0" width="500px" border="0">
				<tr class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'name', 'error')} required">
					<td style="font-weight:bold;" width="200"><label for="name"><g:message code="node.name.label" default="Name" />*</label>: </td>
					<td><g:textField name="name" required="" value="${((params?.name)?params.name:nodeInstance?.name)}"/></td>
				</tr>
				
				<tr class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'description', 'error')} ">
					<td style="font-weight:bold;"><label for="description"><g:message code="node.description.label" default="Description" /></label>: </td>
					<td><g:textField name="description" value="${((params?.description)?params.description:nodeInstance?.description)}"/></td>
				</tr>
				
				<tr>
					<td style="font-weight:bold;"><label for="nodetype"><g:message code="node.nodetype.label" default="Nodetype" />*</label>: </td>
					<td>${nodeInstance?.nodetype?.name}</td>
				</tr>
			
				<tr class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'status', 'error')} required">
					<td style="font-weight:bold;"><label for="status"><g:message code="node.status.label" default="Status" />*</label>: </td>
					<td><g:select name="status" from="${com.dtosolutions.Status?.values()}" keys="${com.dtosolutions.Status.values()*.name()}" required="" value="${((params?.status)?params.status:nodeInstance?.status?.name())}" noSelection="['null': 'Select One']"/></td>
				</tr>
	
				<tr class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'tags', 'error')} ">
					<td style="font-weight:bold;"><label for="tags"><g:message code="node.tags.label" default="Tags" /></label>: </td>
					<td><g:textField name="tags" value="${((params?.tags)?params.tags:nodeInstance?.tags)}"/></td>
				</tr>
				
				<tr>
					<td style="font-weight:bold;" valign=top>Node Parents: </td>
					<td><g:select name="parents" from="${parents}" optionKey="id" optionValue="name" value="${nodeInstance.children.parent.id}" multiple="true" noSelection="['null': 'Select One or More']"/></td>
				</tr>
				<tr>
					<td style="font-weight:bold;" valign=top>Node Children: </td>
					<td><g:select name="children" from="${children}" optionKey="id" optionValue="name" value="${nodeInstance.parents.child.id}" multiple="true" noSelection="['null': 'Select One or More']"/></td>
				</tr>

			</table>
				
				<div id="attributes" style="display:none;"></div>
				
				<fieldset class="form_footer">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
