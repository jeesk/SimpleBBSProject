<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!--
 * @author jeesk
 * 2018年4月2日 下午9:04:42
 * @version
 *
 -->

<script  type="text/javascript">
       var i = 3;
	    var intervalied;
	    
	    setInterval("fun()",1000);
	    function fun(url, time){
	        if(i==0){
	            clearInterval(intervalied);
	            window.location.href = "${pageContext.request.contextPath}/list"; 
	        }
	        document.getElementById("mes").innerHTML=i;
	        i--;
	    }
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	 <span id="mes">3</span>秒钟后自动调转,如果不跳转.请点击 下面的链接
	<a href="${pageContext.request.contextPath}/list">主题列表</a>
</body>
</html>