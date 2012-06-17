<%@ page import="com.dtolabs.NodeType" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'nodeType.label', default: 'NodeType')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		
		<script type="text/javascript">
			$(document).ready(function(){
			    $("#addNewRow").click(function(){
			        // Gets the counter value to increment value while adding each row
			        
			      var newid = parseInt($("#counter").val());
			      var seriescount = parseInt($("#seriescount").val());
		
		          var cloneTheRow = $("#invoiceDetails tr:last").clone();
		
		          $(cloneTheRow).insertAfter("#invoiceDetails tr:last");
		          seriescount = seriescount + 1;
		          
		          // clone the existing row to create new rows
		          newid = newid + 1;
		          $("#counter").val(newid);
		          $("#seriescount").val(seriescount);
		          $("#seriesno").val(seriescount);
		
		          var total = 0.0;
		          $("input[name*='detailAmount']").each(function() {
		          	total += parseFloat($(this).val().replace(/\s/g,'').replace(',','.'));
		           });
		          document.getElementById('detailTotal').innerHTML = formatDollar(total);
		          document.getElementById('totalamountdue').innerHTML = formatDollar(total);
			    });
		
			    $("#recalc").click(function(){
		          var total = 0.0;
		          $("input[name*='detailAmount']").each(function() {
		          	total += parseFloat($(this).val().replace(/\s/g,'').replace(',','.'));
		           });
		          document.getElementById('detailTotal').innerHTML = formatDollar(total);
		          document.getElementById('totalamountdue').innerHTML = formatDollar(total);
			    });
			  	
				$("#bonddatelist").datepicker({ minDate: new Date(2011,1,1) , maxDate: "+8M" });
				
				$("#billingdate").datepicker({ minDate: new Date(2011,1,1) , maxDate: "+8M" });
				$("#duedate").datepicker({ minDate: new Date(2011,1,1) , maxDate: "+8M" });
				
				$("#delete_all").click(function(){ 
					var checked_status = this.checked; 
					$("input[name=delete]").each(function(){ 
						this.checked = checked_status; 
					}); 
				});
				
				$("#memo").resizable({ disabled: true });
				$("#notes").resizable({ disabled: true });
				$("#detailDesc").resizable({ disabled: true });
		
				if($("#voidval").val()=='true'){
					document.getElementById("voidsubmit").disabled = true;
					$("#voidsubmit").addClass("inactive");
			  	}else{
			  		document.getElementById("voidsubmit").disabled = false;
			  		$('#voidsubmit').attr('enabled', 'enabled');
			  	}
		
				if($("#credits").val()){
				    document.getElementById("applycredit").disabled = true
				 	}else{
				 		$('#applycredit').attr('enabled', 'enabled');
				}
		
				$("#contact").change(function(){
					var contact = $(this).val();
					$.ajaxSetup({contentType:"application/json"});
					$.getJSON("${request.contextPath}/bondInvoice/getContactInfo",{id:contact,ajax:'true'},function(json){
						if(json){
				
							var output = ""
							var phone = ""
				
							output += json.fname + ' ' + json.lname + '<br/>'
							if(json.title){
								output += json.title + '<br/>'
							}
							output += json.firm + '<br/>'
							if(json.dept){
								output += json.dept + '<br/>'
							}
							if(json.division){
								output += json.division + '<br/>'
							}
							
							output += json.street
							if(json.suite){
								output += ', ' +json.suite + '<br/>'
							}
							output += json.city + ', ' + json.state + ' ' + json.zip
				
							phone = json.phone
							if(json.phoneExt){
								phone += " EXT. " + json.phoneExt
							}
							document.getElementById('contactInfo').innerHTML = output;
							document.getElementById('contactPhone').innerHTML = phone;
							document.getElementById('contactEmail').innerHTML = json.email;
						}
					})
				});
			});
		</script>
		
	</head>
	<body>

		<div id="create-nodeType" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:form action="save" >
				<table border=0 cellspacing=0 cellpadding=0 valign=top>
					<tr valign=top>
						<td>Name:</td><td><g:textField name="name" required="" value="${nodeTypeInstance?.name}"/></td>
					</tr>			
					<tr valign=top>
						<td>Description:</td><td><g:textField name="description" value="${nodeTypeInstance?.description}"/></td>
					</tr>
					<tr valign=top>
						<td>Image:</td><td><g:select name="image" from="${images}" value="${nodeTypeInstance?.image}"/></td>

					</tr>

				</table>

				
				<fieldset class="form_footer">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>

			</g:form>
		</div>
	</body>
</html>
