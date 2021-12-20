Dropzone.autoDiscover = false;

var currPhoto;
var fileNames = new Map();
var removeFromMap = true;

const mapToObj = m => {
	return Array.from(m).reduce((obj, [key, value]) => {
		obj[key] = value;
		return obj;
	}, {});
};

Map.prototype.getKey = function (targetValue) {
	let desiredKey;
	this.forEach((value, key) => {
		if (value === targetValue)
			desiredKey = key
	});
	return desiredKey;
}

const change = src => {
	try {
		document.getElementById('main_image').src = src;
		currPhoto = src;
	} catch (error) {
	}
}

$(document).ready(function () {
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
		url: "",
		previewTemplate: previewTemplate,
		previewsContainer: "#previews",
		clickable: ".fileinput-button",
		renameFile: function (file) {
			return file.name.substring(0, file.name.lastIndexOf('.')).replaceAll("[^0-9A-Z-a-z]", "") + ".png";
		},
		init: function () {
			var dzClosure = this;

			$.each(fileMap, function (key, value) {
				var file = { name: key, size: value, accepted: true, kind: 'image' }

				dzClosure.displayExistingFile(file, photoLink + key, null, "anonymous", false);
				dzClosure.files.push(file);
				fileNames.set(file.name.substring(file.name.lastIndexOf('/')), file.size);
			})

			$("#submit-all").click(function (e) {
				$(this).prop('disabled', true);
				if (dzClosure.getQueuedFiles().length > 0) {
					e.preventDefault();
					e.stopPropagation();
					dzClosure.options.url = $("#submit-form").attr('action');

					var files = dzClosure.getQueuedFiles();

					files.sort(function (a, b) {
						return ($(a.previewElement).index() > $(b.previewElement).index()) ? 1 : -1;
					})

					dzClosure.processQueue();
				} else {

					const out = Object.create(null);

					fileNames.forEach((value, key) => {
						out[key] = value;
					});

					var form = $("#submit-form");
					form.append($("<input>").attr("type", "hidden").attr("name", "fileNames").val(JSON.stringify(mapToObj(fileNames))));
					form.submit();
				}
			});

			this.on("addedfile", function (file) {
				if (this.files.length && myDropzone.files.length > 10) {
					removeFromMap = true;
					this.removeFile(file);
					document.getElementById("photoButton").style.display = "none";
				} else if (this.files.length && (fileNames.get(file.name) || fileNames.getKey(file.size))) {
					removeFromMap = false;
					this.removeFile(file);
				} else if (this.files.length) {
					var _i, _len;

					for (_i = 0, _len = this.files.length; _i < _len - 1; _i++) {
						if (this.files[_i].name === file.name && this.files[_i].size === file.size) {
							removeFromMap = false;
							this.removeFile(file);
						}
					}

					fileNames.set(file.name.substring(file.name.lastIndexOf('/')), file.size);
				}

				if (this.files.length < 10) {
					document.getElementById("photoButton").style.display = "";
				} else {
					document.getElementById("photoButton").style.display = "none";
				}
			});

			this.on("thumbnail", function (file) {
				file.previewElement.addEventListener("click", function () {
					change(file.dataURL);
					console.log("image changed.")
				});
			});

			this.on('success', function (file) {
				window.location.replace($.trim(file.xhr.responseURL));
			});
		},

	});

	myDropzone.on("removedfile", function (file) {


		if (this.files.length < 10) {
			document.getElementById("photoButton").style.display = "";
		}

		if (file.dataURL === currPhoto) {
			change("/assets/img/image-add-line-pls-upload.png");
		}
		if (removeFromMap) {
			fileNames.delete(file.name);
		}
	});

	myDropzone.on("sendingmultiple", function (data, xhr, formData) {
		formData.append("nombre", $("#name").val());
		formData.append("cantidad", $("#quantity").val());
		formData.append("minLvlEnabled", $("#minLvlEnabled").val());
		formData.append("minLvlOption", $("#minLvlOption").val());
		formData.append("minLvl", $("#lvlMin").val());
		formData.append("precio", $("#price").val());
		formData.append("tags", $("#tags").val());
		formData.append("descripcion", $("#descripcion").val());

		const out = Object.create(null);

		fileNames.forEach((value, key) => {
			out[key] = value;
		});

		formData.append("fileNames", JSON.stringify(out));
	});

	$("#previews").sortable({
		items: '.preview-image',
		cursor: 'move',
		opacity: 0.5,
		containment: '#previews',
		distance: 20,
		tolerance: 'pointer'
	});

});