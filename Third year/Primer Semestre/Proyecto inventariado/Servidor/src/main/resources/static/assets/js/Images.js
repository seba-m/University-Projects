var num = 0;
var arr = [];

function readImage() {
	if (window.File && window.FileList && window.FileReader) {
		var files = event.target.files; //FileList object
		var output = $(".preview-images-zone");

		for (let i = 0; i < files.length; i++) {
			var file = files[i];
			if (!file.type.match('image')) continue;

			if (arr.length === 0 || arr.indexOf(files[i].name) === -1) {
				var picReader = new FileReader();

				picReader.addEventListener('load', function(event) {
					var picFile = event.target;
					//'<div class="preview-image preview-show-' + num + '">' +
					var html = '<div class="preview-image preview-show">' +
						//'<div class="image-cancel" data-no="' + num + '"><i class="bx bx-trash"></i></div>' +
						//'<div class="image-zone"><img id="pro-img-' + num + '" name="images" onclick="change(this.src)" src="' + picFile.result + '"></div>' + '</div>';
						'<div class="image-zone"><img id="pro-img" name="images" onclick="change(this.src)" src="' + picFile.result + '"></div>' + '</div>';
					output.append(html);
					num = num + 1;
				});

				picReader.readAsDataURL(file);
			}
		}

		$("#photoButton").hide();
		$("#photoDeleteButton").show();
		$("#eliminarFotos").val('true');
		//$("#Images").val('');

	} else {
		console.log('Browser not support');
	}
}

const change = src => {
	try {
		document.getElementById('main_image').src = src;
	} catch (error) {
		console.error(error);
	}
}

function clearAll() {

	//for(let i = 0; i < num; i++){
	//$(".preview-image.preview-show-" + i).remove();
	$(".preview-image.preview-show").remove();
	//}

	change("../../../../assets/img/image-add-line-pls-upload.png");
	$("#Images").val('');
	$("#photoButton").show();
	$("#photoDeleteButton").hide();
	$("#eliminarFotos").val('true');
	clearInputFile($("#Images"));

	document.getElementById('Images').addEventListener('change', readImage, false);

	num = 0;
}

function clearInputFile(f) {
	if (f.value) {
		try {
			f.value = ''; //for IE11, latest Chrome/Firefox/Opera...
		} catch (err) { }
		if (f.value) { //for IE5 ~ IE10
			var form = document.createElement('form'),
				parentNode = f.parentNode, ref = f.nextSibling;
			form.appendChild(f);
			form.reset();
			parentNode.insertBefore(f, ref);
		}
	}
}

$(document).ready(function() {

	document.getElementById('Images').addEventListener('change', readImage, false);

	//$(".preview-images-zone").sortable();

	/*$(document).on('click', '.image-cancel', function() {
		let no = $(this).data('no');
		$(".preview-image.preview-show-" + no).remove();
		change("../../../../assets/img/image-add-line-pls-upload.png");
	});*/

});