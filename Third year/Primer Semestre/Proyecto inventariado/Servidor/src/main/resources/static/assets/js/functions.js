$(document).ready(function() {
	$("#uploadFile").change(function() {
		let fileSize = this.files[0].size;
		if (fileSize > (5 * 1024 * 1024)) {
			this.setCustomValidity("File must not exceed 5 MB!");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
		}
	});
	$('.tags').select2({
		theme: 'bootstrap4',
		placeholder: "No tags",
		allowClear: false,
		tags: true,
		width: 'resolve',
		createTag: function(params) {
			return undefined;
		}
	});
	$('.simple-select2').select2({
		theme: 'bootstrap4',
		placeholder: "Tags",
		allowClear: false,
		disabled: true
	});

	$('table.display').DataTable({
		"scrollX": true,
		responsive: true,
	});

	$('#tablaProductos').DataTable({
		"scrollX": true,
		responsive: true,
		"bFilter": false
	});
	$("#name").attr('maxlength', '200');

	$("#price").attr('maxlength', '9');
	$("#quantity").attr('maxlength', '9');
	$("#minlevel").attr('maxlength', '9');
	$("#lvlMin").attr('maxlength', '9');

	$('#btnAlerta').on('click', function () {
		$('#modalAlertData').modal('toggle');
	})

	$("#cancelButtonModal").click(function() {
		$("#modalAlertData").modal('hide');
	});

	$("#deleteButtonModal").click(function() {
		$("#modalAlertData").modal('hide');
		$("#lvlMin").val("");
		$("#minLvlEnabled").val("false");
		$("#minLvlOption option[value='0']").prop('selected', true);
		$("#icBtnAlerta").removeClass("fa-bell").addClass("fa-bell-slash");
		$("#btnAlerta").removeClass("btn-info").addClass("btn-secondary");
	});

	$("#saveButtonModal").click(function() {
		if ($("#lvlMin").val() && $("#minLvlOption").val() !== "0") {
			$("#minLvlEnabled").val("true");
			$("#icBtnAlerta").removeClass("fa-bell-slash").addClass("fa-bell");
			$("#btnAlerta").removeClass("btn-secondary").addClass("btn-info");
		}
		$("#modalAlertData").modal('hide');
	});
	//$('[data-toggle="tooltip"]').tooltip();
	//$('#minlevel').tooltip();
     
});

$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
	$.fn.dataTable.tables({ visible: true, api: true }).columns.adjust();
});

$("#input-fa").fileinput({
	theme: "fas",
	autoOrientImage: false,
	showCaption: false,
	showUpload: false,
	showUploadStats: false,
	uploadUrl: "/file-upload-batch/2",
	allowedFileExtensions: ["jpg", "png", "gif"]
});

$('#quantity').on("cut copy paste", function(e) {
	e.preventDefault();
});
$('#minlevel').on("cut copy paste", function(e) {
	e.preventDefault();
});
$('#price').on("cut copy paste", function(e) {
	e.preventDefault();
});

function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if ((charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
}

function askBeforeDelete(link) {
	if (confirm('you are going to delete this item, are you sure?')) {
		location.replace(link);
	}
}

function addCommas(number) {
	return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")
}

function calcTotal() {
	var priceVal = document.getElementById("price").value;
	var quantityVal = document.getElementById("quantity").value;
	if ((priceVal != null && priceVal > 0) && (quantityVal != null && quantityVal > 0)) {
		document.getElementById('total').value = (addCommas(priceVal * quantityVal));
	} else {
		document.getElementById('total').value = '';
	}
}