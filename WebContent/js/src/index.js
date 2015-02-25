$(document).ready(function() {
	$("#alzBtn").click(function() {
		var msg = $("#txtMsg").val();
		
		if (msg) {
			$("#rstMsg").html("Analyzing...");
			$.ajax({
				type: "POST",
				url: "api/nlp",
				data: msg,
				contentType: "text/plain",
				success: function(msg) {
					$("#rstMsg").html(msg);
				}
			});			
		}
	});
});