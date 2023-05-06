<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>결과창</title>

</head>
<body>
	<h1>업로드 완료</h1>
	<label> 아이디 :</label>
		<input type="text" name="id" value='${map.id}' readonly><br>
	<label>이름 :</label>
		<input type="text" name="name" value='${map.name}' readonly><br>
		
		<div class="result-images">
		<c:forEach var="imageFileName" items="${map.fileList}">
			<img src="${contextPath }/download2?imageFileName=${imageFileName}">
			<br><br>
		</c:forEach>
		</div>
		
		<a href='${contextPath }/form'>다시업로드하기</a>
	
	
</body>
</html>