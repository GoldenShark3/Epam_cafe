<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<html>
<head>
    <title><fmt:message key="title.accessDenied"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
    <c:import url="../../parts/navbar.jsp"/>
    <div class="alert alert-danger mt-4 text-center" role="alert">
        <h3 class="alert-heading"><fmt:message key="title.accessDenied"/></h3>
        <hr>
        <p class="mb-0"><fmt:message key="serverMessage.contactUs"/></p>
    </div>
    <c:import url="../../parts/footer.jsp"/>
</body>
</html>
