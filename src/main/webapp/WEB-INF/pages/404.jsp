<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:set var="page" value="/WEB-INF/pages/404.jsp" scope="request"/>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<!doctype html>
<html>
<head>
    <title><fmt:message key="title.error"/></title>
    <c:import url="../parts/header.jsp"/>
</head>
<body>
<c:import url="../parts/navbar.jsp"/>

<div class="d-flex justify-content-center align-items-center" id="main">
    <h1 class="mr-3 pr-3 align-top border-right inline-block align-content-center">404</h1>
    <div class="inline-block align-middle">
        <h2 class="font-weight-normal lead" id="desc">The page you requested was not found.</h2>
    </div>
</div>
<c:import url="../parts/footer.jsp"/>
</body>
</html>
