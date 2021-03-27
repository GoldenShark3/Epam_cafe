<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="page" value="/WEB-INF/pages/error.jsp" scope="request"/>

<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<html>
<head>
    <title><fmt:message key="title.error"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>

<div class="alert alert-warning mt-4 text-center" role="alert">
    <h3 class="alert-heading">ERROR</h3>
    <p><fmt:message key="title.error"/></p>
    <hr>
    <p class="me-2">TRY LATER</p>
</div>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
