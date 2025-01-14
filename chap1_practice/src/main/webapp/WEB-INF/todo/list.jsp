<%--
  Created by IntelliJ IDEA.
  User: 이승헌
  Date: 2023-12-31
  Time: 오전 6:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>List Page</h1>
    <ul>
        <c:forEach var="dto" items="${List}">
            <li>${dto}</li>
        </c:forEach>
        <!--
        <c:if test="${List.size()%2==0}">
            짝수
        </c:if>
        <c:if test="${List.size()%2!=0}">
            홀수
        </c:if>
        -->
        <!--
        <c:choose>
            <c:when test="${List.size()%2==0}">
                짝수
            </c:when>
            <c:otherwise>
                홀수
            </c:otherwise>

        </c:choose>
        -->
    </ul>
    <c:set var="target" value="5"></c:set>
    <ul>
        <c:forEach var="num" begin="1" end="10">
            <c:if test="${num==target}">
                num is target
            </c:if>
        </c:forEach>
    </ul>
</body>
</html>
