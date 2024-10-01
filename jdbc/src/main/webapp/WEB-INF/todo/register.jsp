<%--
  Created by IntelliJ IDEA.
  User: 이승헌
  Date: 2024-01-03
  Time: 오전 4:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <form action="/todo/register" method="post">
        <div>
            <input type="text" name="title" placeholder="Insert Title">
        </div>
        <div>
            <input type="date" name="dueDate">
        </div>
        <div>
            <button type="reset">RESET</button>
            <button type="submit">REGISTER</button>
        </div>
    </form>
</body>
</html>
