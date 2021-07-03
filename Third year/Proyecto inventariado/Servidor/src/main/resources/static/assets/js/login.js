function myAlertTop() {
	$(".myAlert-top").show();
	setTimeout(function() {
		$(".myAlert-top").hide();
	}, 0);
}

function alertas() {
	$.bootstrapGrowl("another message, yay!", {
		ele: 'body', // which element to append to
		type: 'info', // (null, 'info', 'error', 'success')
		offset: { from: 'top', amount: 20 }, // 'top', or 'bottom'
		align: 'right', // ('left', 'right', or 'center')
		width: 250, // (integer, or 'auto')
		delay: 4000,
		allow_dismiss: true,
		stackup_spacing: 10 // spacing between consecutively stacked growls.
	});
}

function verificar() {
	if (document.getElementById('password').value ==
		document.getElementById('confirm_password').value) {
		document.getElementById('message').style.color = 'green';
		document.getElementById('message').innerHTML = 'matching';
	} else {
		document.getElementById('message').style.color = 'red';
		document.getElementById('message').innerHTML = 'not matching';
	}
}
