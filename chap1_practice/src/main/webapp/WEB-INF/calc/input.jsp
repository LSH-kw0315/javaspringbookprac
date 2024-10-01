<%--
  Created by IntelliJ IDEA.
  User: 이승헌
  Date: 2023-12-30
  Time: 오전 5:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/calc/makeResult" method="post">
        <input type="number" name="left">
        <input type="number" name="right">
        <button type="submit">Send</button>
    </form>
</body>
</html>
