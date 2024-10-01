<%--
  Created by IntelliJ IDEA.
  User: 이승헌
  Date: 2024-01-03
  Time: 오전 3:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Todo List</title>
</head>
<body>
    <h1>Todo List</h1>
    <h2>${appName}</h2>
    <h2>${loginInfo}</h2>
    <h3>${loginInfo.mname}</h3>
   <c:forEach items="${dtoList}" var="dto">
       <li>
           <span><a href="/todo/read?tno=${dto.tno}">${dto.tno}</a></span>
           <span>${dto.title}</span>
           <span>${dto.dueDate}</span>
           <span>${dto.finished? "DONE":"NOT YET"}</span>
       </li>
   </c:forEach>
    <form action="/logout" method="post">
        <button type="submit">LOGOUT</button>
    </form>
</body>
</html>
