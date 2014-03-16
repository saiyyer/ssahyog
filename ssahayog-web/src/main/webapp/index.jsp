<!doctype html>
<html lang="us">
<head>
<link rel="stylesheet" href="css/overcast/jquery-ui-1.10.4.custom.min.css">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.4.custom.min.js"></script>
<!-- <script type="text/javascript" src="js/jquery-1.11.0.min.js"></script> -->
<script type="text/javascript">
	function fetchVolunteerDetails(buttonItemClicked, data) {
		var buttonValue = buttonItemClicked.val();
		var volunteerTable = $("#volunteertbl");
		if (buttonValue == "Clear") {
			buttonItemClicked.val("Get Volunteer detailsQ");
			volunteerTable.remove();
		} else {
			buttonItemClicked.val("Clear");
			$
					.ajax({
						type : "GET",
						url : "TestServlet",
						contentType : "application/json;charset=utf-8",
						data : data,
						success : function(response) {
							var volunteers = response.result.records;
							if (volunteers != null && volunteers.length > 0) {
								$("#volunteertbl").remove();
								$("#volunteerDetails")
										.append(
												"<table id=\"volunteertbl\"><th><td>ID</td><td>Name</td></th></table>");
								$.each(volunteers,
										function(index, volunteer) {
											var volunteerRow = "<tr> <td>"
													+ volunteer.VOLUNTEERID
													+ "</td><td>"
													+ volunteer.VOLUNTEERNAME
													+ "</tr>";
											$("#volunteertbl").append(
													volunteerRow);
										});
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							alert("@error");
							console.log(textStatus, errorThrown);
						},
						complete : function() {
							volunteerTable.show();
						}
					});
		}
	}

	$(document)
			.ready(
					function() {
						// This is the old method where we are providing the query to 
						// collect the results
						// alternate method listed below
						$("#getVolunteerDtlsQ")
								.click(
										function() {
											var queryMap = {
												"query" : "SELECT VOLUNTEERID as VolunteerID, VOLUNTEER_NAME as VolunteerName, RES_ADDRESS as ResidentialAddress FROM VOLUNTEER_DETAILS"
											};
											fetchVolunteerDetails($(this),
													queryMap)
										});
						// newer method of just injecting the action parameter with
						// 'givenName' in the form of text, which would run and return the same query as 
						// above.
						$("#getVolunteerDtls").click(function() {
							nameEntered = $("#txtGivenName").val();
							if (nameEntered == null) {
								alert("Nothing entered to search");
								$("#txtGivenName").focus();
								return;
							}
							var queryMap = {
								"givenName" : nameEntered,
								"action" : "getVolunteerDtls"
							};
							fetchVolunteerDetails($(this), queryMap);
						});

					});
</script>
</head>
<body>
	<input type="hidden" name="query" id="query"
		value="SELECT VOLUNTEERID as VolunteerID, VOLUNTEER_NAME as VolunteerName, RES_ADDRESS as ResidentialAddress FROM VOLUNTEER_DETAILS " />
	<input type="button" name="getVolunteerDtlsQ" id="getVolunteerDtlsQ"
		value="Get Volunteer details Q">
	<p />
	<input type="text" name="txtGivenName" id="txtGivenName" />
	<input type="button" name="getVolunteerDtls" id="getVolunteerDtls"
		value="Get Volunteer Details">
	<div id="volunteerDetails"></div>
</body>
</html>
