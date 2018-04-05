<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!--
 * @author jeesk
 * 2018年3月30日 下午5:53:08
 * @version 1.0
 *
 -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>  
        
        <form action="/login" method="post">
                        帐号:   <input type="text" name="username" required value=""/><br/>
                        密码:   <input type="text" name="password" required value=""/><br/>
                <input type="submit" value="登录"/>
        
        </form>

    
        
</body>
</html>