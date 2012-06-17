<%@ page import="com.dtolabs.NodeType" %>
<%@ page import="com.dtolabs.Attribute" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		
  	<script type="text/javascript">
  	window.onload = init;
  	function init() {
			getAttributes();
  	}

  	/*
  	function getFormFields(){
  		getAttributes();
  	}
  	*/

  	function getAttributes(){
  	  	var nodetype = "${nodeTypeInstance?.id}"
		$.ajaxSetup({contentType:"application/json"});
		$.getJSON("${request.contextPath}/nodeType/getTemplateAttributes",{templateid:nodetype,ajax:'true'},function(json){
			if(json){
				var div = document.getElementById("attributes");
				div.innerHTML = '';
				var table = document.createElement('table');
				table.style.width = '480px';
				table.style.border = '0px';
				var attList = json[0].attList;
				var atts = json[0].atts;
				
				for (i in atts){
					var row = document.createElement("tr");
					row.id='att'+atts[i].tid+'_row';
					
					var cell1 = document.createElement("td");
					cell1.id='att'+atts[i].tid+'_cell1';
					cell1.width='95px';

					var delButton = document.createElement('input');
					delButton.type='button';
					delButton.setAttribute("id",atts[i].tid);
					delButton.value = '-';
					delButton.style.position='relative';
					delButton.style.left='52px';
					delButton.onclick =  function () {deleteTemplateAttribute(this.id)};
					cell1.appendChild(delButton);
					row.appendChild(cell1);

					var cell2 = document.createElement("td");
					cell2.id='att'+atts[i].tid+'_cell2';
					cell2.style.float="left";
					
					var span1=document.createElement('span');
					span1.className="styled-select";
					
					var select = document.createElement('select');
					var opt = document.createElement('option');
					opt.innerHTML="Select A Value";
					opt.setAttribute('value','NULL');
					select.appendChild(opt)
					for(b in attList){
						var opt = document.createElement('option');
						opt.innerHTML=attList[b].name;
						opt.setAttribute('value',attList[b].id);
						if(atts[i].id==attList[b].id){
							opt.selected = true
						}
						select.appendChild(opt)
					}
					span1.appendChild(select);
					cell2.appendChild(span1);
					row.appendChild(cell2);
					table.appendChild(row);
				}

				var i = atts.length
				var row = document.createElement("tr");
				row.id='att'+i+'_row';
				
				var cell1 = document.createElement("td");
				cell1.id='att'+i+'_cell1';
				cell1.width='95px';
				
				var addButton = document.createElement('input');
				addButton.type='button';
				addButton.value = '+';
				addButton.style.position='relative';
				addButton.style.left='52px';
				addButton.onclick =  function () {addTemplateAttribute(nodetype,document.getElementById("new_attribute").value)};
				cell1.appendChild(addButton);
				row.appendChild(cell1);

				var cell2 = document.createElement("td");
				cell2.id='att'+i+'_cell2';

				var span1=document.createElement('span');
				span1.className="styled-select";

				
				var select = document.createElement('select');
				select.setAttribute("id","new_attribute");
				select.setAttribute("name","new_attribute");
				var opt = document.createElement('option');
				opt.innerHTML="Select A Value";
				opt.setAttribute('value','NULL');
				select.appendChild(opt)
				for(b in attList){
					var opt = document.createElement('option');
					opt.innerHTML=attList[b].name;
					opt.setAttribute('value',attList[b].id);
					select.appendChild(opt)
				}
				span1.appendChild(select);
				cell2.appendChild(span1);
				row.appendChild(cell2);
				table.appendChild(row);

				div.appendChild(table);
			}
		});
  	}

  	function addTemplateAttribute(nodetype,attId){
  	    var params = {};
  	    params.template = nodetype;
  	    params.attribute = attId;
  	    var jsonData = window.JSON.stringify(params);
        $.ajax({ 
            type: "POST",
            dataType: "json",
            data: jsonData,
            url: "${request.contextPath}/templateAttribute/saveTemplateAttribute/",
            success: function(data){
               alert("Attribute added successfully!")
               getAttributes();
            }
        });
  	}

  	function deleteTemplateAttribute(templateAtt){
  	    var params = {};
  	    params.id = templateAtt;
  	    var jsonData = window.JSON.stringify(params);
        $.ajax({ 
            type: "POST",
            dataType: "json",
            data: jsonData,
            url: "${request.contextPath}/templateAttribute/deleteTemplateAttribute/",
            success: function(data){  
            	alert("Attribute deleted successfully!")      
               getAttributes();
            }
        });

  	}
	</script>
		
	</head>
	<body>

		<div id="edit-nodeType" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:form method="post" >
				<g:hiddenField name="id" value="${nodeTypeInstance?.id}" />
				<g:hiddenField name="version" value="${nodeTypeInstance?.version}" />
				<table border=0 cellspacing=0 cellpadding=0 valign=top>
					<tr valign=top>
						<td width='100px'>Name:</td><td><g:textField name="name" required="" value="${nodeTypeInstance?.name}"/></td>
					</tr>			
					<tr valign=top>
						<td>Description:</td><td><g:textField name="description" value="${nodeTypeInstance?.description}"/></td>
					</tr>
					<tr valign=top>
						<td>Image:</td><td><span class="styled-select"><g:select name="image" from="${images}" value="${nodeTypeInstance?.image}"/></span></td>
					</tr>

				</table>

				<div id="attributes"></div>
				
				<fieldset class="form_footer">
					<span class="fake_button"><g:link action="show" id="${nodeTypeInstance?.id}"><g:message code="default.button.clone.label" default="Cancel" /></g:link></span>
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
