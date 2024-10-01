<%--
  Created by IntelliJ IDEA.
  User: 이승헌
  Date: 2023-12-30
  Time: 오전 5:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>NUM1 ${param.left}</h1>
    <h1>NUM2 ${param.right}</h1>
    <h1>result ${Integer.parseInt(param.left) + Integer.parseInt(param.right)}</h1>
</body>
</html>
