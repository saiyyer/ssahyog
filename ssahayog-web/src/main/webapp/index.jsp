<html>
<head>
<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
<script type="text/javascript">
		$(document).ready(
				function () {
					$("#getVolunteerDtls").click(function() {
						var buttonValue = $("#getVolunteerDtls").val();
						//alert(buttonValue);
						if(buttonValue=="Clear"){
							$("#getVolunteerDtls").val("Get Volunteer details");
							$("#volunteertbl").remove();
						} else if(buttonValue=="Get Volunteer details"){
							$("#getVolunteerDtls").val("Clear");
						$.ajax({
							type:"GET",
							url: "TestServlet",
							contentType: "application/json;charset=utf-8",
							data:{query :"SELECT VOLUNTEERID as VolunteerID, VOLUNTEER_NAME as VolunteerName, RES_ADDRESS as ResidentialAddress FROM VOLUNTEER_DETAILS "},
// 							beforeSend : function() {
// 				                $("#mytable").remove();
// 				            },
							success:function(response){
								var volunteers = response.result.records;
								//console.log(response);
								if(volunteers != null && volunteers.length > 0){
									$("#volunteertbl").remove();
									$("#volunteerDetails").append("<table id=\"volunteertbl\"><th><td>ID</td><td>Name</td></th></table>");												
									$.each(volunteers, function(index, volunteer){
											var volunteerRow = "<tr> <td>"+volunteer.VOLUNTEERID+"</td><td>"+volunteer.VOLUNTEERNAME+"</tr>";
											//console.log("Id: "+volunteer.VolunteerID+", Name: "+VolunteerName);
											//alert("Id: "+volunteer.VolunteerID+", Name: "+VolunteerName);
											$("#volunteertbl").append(volunteerRow);
											//alert(volunteer.VOLUNTEERNAME);
									});
								}
							},
							error: function(jqXHR, textStatus,errorThrown ){
								alert("@error");
								console.log(textStatus, errorThrown);
							},
							complete: function(){
								$("#volunteertbl").show();
							}
						});
						}
					});
				}
				);
		
	</script>
</head>
<body>
		<input type="hidden" name="query" id="query"
			value="SELECT VOLUNTEERID as VolunteerID, VOLUNTEER_NAME as VolunteerName, RES_ADDRESS as ResidentialAddress FROM VOLUNTEER_DETAILS " />
		<input type="button" name="getVolunteerDtls" id="getVolunteerDtls"
			value="Get Volunteer details">
		<div id="volunteerDetails"></div>
</body>
</html>
