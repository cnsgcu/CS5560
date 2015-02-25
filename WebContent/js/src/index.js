$(document).ready(function() {
	$("#alzBtn").click(function() {
		$("#rstMsg").html("Analyzing...");
		var msg = $("#txtMsg").val();
		
		if (msg) {
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