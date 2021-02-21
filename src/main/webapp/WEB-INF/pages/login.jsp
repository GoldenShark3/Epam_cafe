<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<fmt:setLocale value="ru_RU"/>
<fmt:setBundle basename="content_ru_RU"/>

<html>
<head>
    <c:import url="../parts/header.jsp"/>
    <title><fmt:message key="login.title"/></title>
</head>
<body>
<c:import url="../parts/navbar.jsp"/>

<div class="container-fluid d-flex justify-content-center mt-5">
    <form name="login" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-25 border-dark" novalidate>
        <h2 class="d-flex justify-content-center"><fmt:message key="login.caption"/></h2>
        <input type="hidden" name="command" value="login">
        <div>
            <label class="form-label" for="loginLabel"><fmt:message key="email.label"/></label>
            <input type="email" class="form-control" placeholder="<fmt:message key="placeholder.email"/>" name="email"
                   id="loginLabel" pattern="([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}" required/>
            <div class="invalid-feedback">
                <fmt:message key="invalid.email"/>
            </div>
        </div>
        <div>
            <label class="form-label" for="passwordLabel"><fmt:message key="password.label"/></label>
            <input type="password" name="password" class="form-control" id="passwordLabel" required/>
            <div class="invalid-feedback">
                <fmt:message key="invalid.password"/>
            </div>
        </div>
        <div class="form-group mt-2">
            <button type="submit" class="btn btn-dark w-100"><fmt:message key="button.login"/></button>
            <div class="mt-2 d-flex justify-content-center">
                <span>
                    <a href="<c:url value="/cafe?command=to_registration"/>"><fmt:message key="link.registration"/></a>
                </span>
            </div>
        </div>
    </form>
</div>

<c:import url="../parts/footer.jsp"/>
</body>
</html>
