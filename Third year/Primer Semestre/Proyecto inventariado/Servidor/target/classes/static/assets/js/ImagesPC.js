Dropzone.autoDiscover = false;

var currPhoto;

const change = src => {
	try {
		document.getElementById('main_image').src = src;
		currPhoto = src;
	} catch (error) {
		console.error(error);
	}
}

$(document).ready(function() {
	var previewNode = document.querySelector("#template");
	previewNode.id = "";
	var previewTemplate = previewNode.parentNode.innerHTML;
	previewNode.parentNode.removeChild(previewNode);

	var myDropzone = new Dropzone(document.body, {
		addRemoveLinks: false,
		uploadMultiple: true,
		autoProcessQueue: false,
		preventDuplicates: true,
		maxfiles: 10,
		parallelUploads: 10,
		maxFilesize: 5,
		thumbnailWidth: 1920,
		thumbnailHeight: 1080,
		acceptedFiles: ".png, .jpeg, .jpg",
		url: "some default url xd",
		previewTemplate: previewTemplate,
		previewsContainer: "#previews",
		clickable: ".fileinput-button",
		renameFile: function(file) {
			return file.name.substring(0, file.name.lastIndexOf('.')).replaceAll("[^0-9A-Z-a-z]", "");
		},
		init: function() {
			var dzClosure = this;

			$('#submit-form').one('submit', function(e) {
				e.preventDefault();
				$('#submit-all').prop('disabled', true);
				if (dzClosure.getQueuedFiles().length > 0) {
					e.preventDefault();
					e.stopPropagation();
					dzClosure.options.url = $("#submit-form").attr('action');
					dzClosure.processQueue();
				} else {
					$(this).submit();
				}
			});

			this.on("thumbnail", function(file) {
				file.previewElement.addEventListener("click", function() {
					change(file.dataURL);
				});
			});

			this.on('success', function(file) {
				window.location.replace($.trim(file.xhr.responseURL));
			});
		},

	});

	myDropzone.on("removedfile", function(file) {
		if (this.files.length < 10) {
			document.getElementById("photoButton").style.display = "";
		}

		if (file.dataURL === currPhoto) {
			change("../../../../assets/img/image-add-line-pls-upload.png");
		}

	});

	myDropzone.on("addedfile", function(file) {
		if (this.files.length && myDropzone.files.length > 10) {
			this.removeFile(file);
			document.getElementById("photoButton").style.display = "none";
		} else if (this.files.length) {
			var _i, _len;
			for (_i = 0, _len = this.files.length; _i < _len - 1; _i++) {
				if (this.files[_i].name === file.name && this.files[_i].size === file.size && this.files[_i].lastModifiedDate.toString() === file.lastModifiedDate.toString()) {
					this.removeFile(file);
				}
			}

			if (this.files.length < 10) {
				document.getElementById("photoButton").style.display = "";
			} else {
				document.getElementById("photoButton").style.display = "none";
			}
		}
	});

	myDropzone.on("sendingmultiple", function(data, xhr, formData) {
		formData.append("nombre", $("#name").val());
		formData.append("cantidad", $("#quantity").val());
		formData.append("minLvlEnabled", $("#minLvlEnabled").val());
		formData.append("minLvlOption", $("#minLvlOption").val());
		formData.append("minLvl", $("#lvlMin").val());
		formData.append("precio", $("#price").val());
		formData.append("tags", $("#tags").val());
		formData.append("descripcion", $("#descripcion").val());
	});

	$("#previews").sortable({
		items: '.preview-image',
		cursor: 'move',
		opacity: 0.5,
		containment: '#previews',
		distance: 20,
		tolerance: 'pointer',
		/*stop: function() {var queue = myDropzone.files;var newQueue = [];$('#uploadzone .dz-preview .dz-filename [data-dz-name]').each(function(count, el) {var name = el.innerHTML getAttribute('data-name');queue.forEach(function(file) {if (file.name === name) {newQueue.push(file);}});});myDropzone.files = newQueue;}*/
	});

});
