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
  		getNodeParents();
  	}
  		
  	function getNodeParents(){
  		var solution = $("#solution").val();
		$.ajaxSetup({contentType:"application/json"});
		$.getJSON("${request.contextPath}/node/getNodeParents",{id:solution,ajax:'true'},function(json){
			if(json){
				var select = document.getElementById("parent");
				select.innerHTML = '';
				var opt = document.createElement('option');
				opt.innerHTML="Select A Value";
				opt.setAttribute('value','NULL');
				select.appendChild(opt)
				for(var i=0;i<json.length;i++){
					var j = json[i];
					var opt = document.createElement('option');
					opt.innerHTML=j.name;
					opt.setAttribute('value',j.id);
					select.appendChild(opt)
				}
			}
		});
  	}
  	
  	function getTemplates(){
  		var nodetype = $("#nodetype").val();
  		if(nodetype!='null'){
			$.ajaxSetup({contentType:"application/json"});
			$.getJSON("${request.contextPath}/node/getTemplates",{id:nodetype,ajax:'true'},function(json){
				if(json){
					var select = document.getElementById("template");
					select.innerHTML = '';
					var opt = document.createElement('option');
					opt.innerHTML="Select A Value";
					opt.setAttribute('value','NULL');
					select.appendChild(opt)
					for(var i=0;i<json.length;i++){
						var j = json[i];
						var opt = document.createElement('option');
						opt.innerHTML=j.name;
						opt.setAttribute('value',j.id);
						select.appendChild(opt)
					}
				}
			});
			$("#template_wrapper").show();
			$("#attributes").hide();
  		}else{
  			$("#template_wrapper").hide();
  			$("#attributes").hide();
  	  	}
  	}

  	function getAttributes(){
  	  	var node = "${nodeInstance?.id}"
  		var template = $("#template").val();
  	  	if(template!=null){
			$.ajaxSetup({contentType:"application/json"});
			$.getJSON("${request.contextPath}/node/getTemplateAttributes",{templateid:template,node:nodeajax:'true'},function(json){
				if(json){
					var div = document.getElementById("attributes");
					div.innerHTML = '';
					var table = document.createElement('table');
					table.style.width = '500px';
					table.style.border = '0px';
					for(var i=0;i<json.length;i++){
						var j = json[i];
						var row = document.createElement("tr");
						row.id='att'+j.attid+'_row'
						
						var cell1 = document.createElement("td");
						cell1.id='att'+j.attid+'_cell1'
						
						cell1.innerHTML = j.val;
						row.appendChild(cell1);

						var cell2 = document.createElement("td");
						cell2.id='att'+j.attid+'_cell2'
						
						// input hidden - attid_require
						var require = document.createElement('input');
						require.type='hidden';
						require.name='att'+j.attid+'_require';
						require.value = j.required;
						require.id='att'+j.attid+'_require';
						
						// input hidden - attid_filter
						var filter = document.createElement('input');
						filter.type='hidden';
						filter.name='att'+j.attid+'_filter';
						filter.value = j.filter;
						filter.id='att'+j.attid+'_filter';
						
						// input text - attid
						var input = document.createElement('input');
						input.type='text';
						input.name='att'+j.attid;
						input.id='att'+j.attid;
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

				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'name', 'error')} required">
					<label for="name">
						<g:message code="node.name.label" default="Name" />
						<span class="required-indicator">*</span>
					</label>
					<g:textField name="name" required="" value="${nodeInstance?.name}"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'description', 'error')} ">
					<label for="description">
						<g:message code="node.description.label" default="Description" />
						
					</label>
					<g:textField name="description" value="${nodeInstance?.description}"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'nodetype', 'error')} required">
					<label for="nodetype">
						<g:message code="node.nodetype.label" default="Nodetype" />
						<span class="required-indicator">*</span>
					</label>
					<g:select id="nodetype" name="nodetype.id" from="${com.dtosolutions.NodeType.list()}" optionKey="id" required="" value="${nodeInstance?.nodetype?.id}" class="many-to-one" onchange="getTemplates();"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'template', 'error')} required">
					<label for="template">
						<g:message code="node.template.label" default="Template" />
						<span class="required-indicator">*</span>
					</label>
					<g:select id="template" name="template.id" from="${com.dtosolutions.Template.list()}" optionKey="id" required="" value="${nodeInstance?.template?.id}" class="many-to-one" onchange="getAttributes();"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'status', 'error')} required">
					<label for="status">
						<g:message code="node.status.label" default="Status" />
						<span class="required-indicator">*</span>
					</label>
					<g:select name="status" from="${com.dtosolutions.Status?.values()}" keys="${com.dtosolutions.Status.values()*.name()}" required="" value="${nodeInstance?.status?.name()}"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'importance', 'error')} required">
					<label for="importance">
						<g:message code="node.importance.label" default="Importance" />
						<span class="required-indicator">*</span>
					</label>
					<g:select name="importance" from="${com.dtosolutions.Importance?.values()}" keys="${com.dtosolutions.Importance.values()*.name()}" required="" value="${nodeInstance?.importance?.name()}"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'tags', 'error')} ">
					<label for="tags">
						<g:message code="node.tags.label" default="Tags" />
						
					</label>
					<g:textField name="tags" value="${nodeInstance?.tags}"/>
				</div>
				
				<div class="fieldcontain ${hasErrors(bean: nodeInstance, field: 'parent', 'error')} ">
					<label for="parent">
						<g:message code="node.parent.label" default="Parent" />
						
					</label>
					<g:select id="parent" name="parent.id" from="${com.dtosolutions.Node.list()}" optionKey="id" value="${nodeInstance?.parent?.id}" class="many-to-one" noSelection="['null': '']"/>
				</div>
				


				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
