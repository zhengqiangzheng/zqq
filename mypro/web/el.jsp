<%--
  Created by IntelliJ IDEA.
  User: zq
  Date: 2018/8/18/018
  Time: 23:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta http-equiv="content-type" content="text/html";charset="UTF-8">
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    request.setAttribute("d1","mysql");

%>
<c:if test="${1==1}">
    mysq
    <%
        ${d1} ;
    %>
    ${d1}
</c:if>



</body>



</html>
