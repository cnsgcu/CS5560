$(document).ready(function() {
	$('button').click(function() {
		var msg = $('#txtMsg').val();
		
		if (msg) {
			$('#result').html('');
			$('#result').removeData('typed');
			$('#result').typed({
				strings: ['Writing..!'],
				typeSpeed: 0,
				callback: function() {
					$.ajax({
						type: "POST",
						url: "api/transform",
						data: msg,
						contentType: "text/plain",
						success: function(msg) {
							$('#result').html('');
							$('#result').removeData('typed');
							
							$('#result').typed({
								strings: [msg],
								typeSpeed: 0
							});
						}
					});						
				}
			});	
		}
	});
});

