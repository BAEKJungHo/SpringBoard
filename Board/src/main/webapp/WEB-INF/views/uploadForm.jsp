<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UPLOAD</title>
</head>
<body>
	<form name="uploadForm" action="<c:url value="/uploadForm" />" method="post" enctype="multipart/form-data" >
		<input type="file" name="file" />
		<input type="submit" />
	</form>
	SavedFileName : ${savedFileName}
	
	<!--  iframe 방식 -->
	<!-- form태그의 target 속성명과 iframe의 name명이 같아야 한다. -->
	<hr />
	<form name="uploadForm" id="form2" action="<c:url value="/uploadForm" />" method="post" enctype="multipart/form-data" target="ifr">
		<input type="hidden" name="type" value="ifr" />
		<input type="file" name="file" />
		<input type="submit" />
	</form>
	IFR - SavedFileName : <span id="upfile"></span>
	<iframe src="" frameborder="0" width="0" height="0" name="ifr"></iframe>
	
	<hr />
	<div class="fileDrop">Drop!!</div>
	<div class="uploadedList"></div>
	
	<!-- ajax 방식 -->
	<form name="uploadForm" id="form3" action="<c:url value="/uploadForm" />" method="post" enctype="multipart/form-data">
		<input type="hidden" name="type" value="ajax" />
		<input type="file" name="file" />
		<input type="submit" value="ajax로 제출"/>
	</form>
	<div id="percent">0 %</div>
	<div id="status">ready</div>
	ajax - SavedFileName : <span id="ajax_upfile"></span>
	
	
	<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.min.js"></script> 
	<script src="resources/plugins/jQuery/jQuery.form.min.js"></script>	
	<script>
		window.setUploadedFile = (filename) => {
			document.getElementById('upfile').innerHTML = filename;
			document.getElementById("form2").reset();
		};
		
		const $fileDrop = $('div.fileDrop'),
			  $uploadedList = $('div.uploadedList');
		
		$fileDrop.on('dragover dragenter', (evt) => {
			evt.preventDefault();
			$fileDrop.css("border", "1px dotted green");
		});
		
		$fileDrop.on('dragleave', (evt) => {
			evt.preventDefault();
			$fileDrop.css("border", "none");
		});
		
		$fileDrop.on('drop', (evt) => {
			evt.preventDefault();
			console.debug("drop =>", evt.originalEvent.dataTransfer.files);
		});
		
		const percent = $('#percent'), 
			  status = $('#status');
		$('#form3').ajaxForm({
			beforeSend: function() {
				$status.empty();
				$percent.html('0%');
			},
			uploadProgress: function(event, position, total, percentComplete) {
				status.html('uploading ..... ')
				percent.html(percentComplete + '%');
			},
			complete: function(xhr) {
				$status.html(xhr.responseText);
			}
		});
	</script>

	<!-- iframe 일때 -->	
	<c:if test="${type eq 'ifr'}">
		<script>
			console.debug("---------ifr script----------");
			parent.setUploadedFile('${savedFileName}');
		</script>
	</c:if>
</body>
</html>