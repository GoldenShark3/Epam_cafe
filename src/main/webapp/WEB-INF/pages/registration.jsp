<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<fmt:setLocale value="ru_RU"/>
<fmt:setBundle basename="content_ru_RU"/>

<html>
<head>
    <c:import url="../parts/header.jsp"/>
    <title><fmt:message key = "registration.title"/></title>
</head>
<body>
    <c:import url="../parts/navbar.jsp"/>

    <div class="container-fluid d-flex justify-content-center mt-3">
        <form name="registration" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-30 " novalidate>
            <div class="d-flex justify-content-center mb-0">
                <h2><fmt:message key="registration.title"/></h2>
            </div>
            <input type="hidden" name="command" value="registration">
            <div class="row mt-0">
            <div class="col-6">
                <label for="firstNameLabel" class="form-label"><fmt:message key="firstName.label"/></label>
                <input type="text" class="form-control" placeholder="<fmt:message key="placeholder.firstName"/>"
                    name = "firstName" id="firstNameLabel" pattern="^[A-Za-zА-Яа-яЁё']{2,45}?$" required/>
                <div class="invalid-feedback">
                    <fmt:message key="invalid.firstName"/>
                </div>
            </div>
            <div class="col-6">
                <label for="lastNameLabel" class="form-label"><fmt:message key="lastName.label"/></label>
                <input type="text" class="form-control" placeholder="<fmt:message key="placeholder.lastName"/>"
                       name = "lastName" id="lastNameLabel" pattern="^[A-Za-zА-Яа-яЁё']{2,90}?$" required/>
                <div class="invalid-feedback">
                    <fmt:message key="invalid.lastName"/>
                </div>
            </div>
            </div>
            <div class="form-group mt-2">
                <label for="username" class="form-label"><fmt:message key="username.label"/></label>
                <input type="text" name="username" id="username" pattern="^[A-Za-z0-9_]{5,30}" class="form-control"
                       placeholder="<fmt:message key="placeholder.username"/>" required/>
                <div class="invalid-feedback">
                    <fmt:message key="invalid.username"/>
                </div>
            </div>
            <div class="row mt-2">
                <div class="col-6">
                    <label for="email" class="form-label"><fmt:message key="email.label"/></label>
                    <input type="email" id="email" name="email" class="form-control"
                           placeholder="<fmt:message key="placeholder.email"/>" required
                           pattern="^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"/>
                    <div class="invalid-feedback">
                        <fmt:message key="invalid.email"/>
                    </div>
                </div>
                <div class="col-6">
                    <label for="number" class="form-label"><fmt:message key="phone.label"/></label>
                    <div class="input-group has-validation">
                        <input type="text" id="number" name="number" class="form-control"
                               pattern="^\+375((44)|(33)|(29)|(25))[0-9]{7}$" value="+375" required/>
                        <div class="invalid-feedback">
                            <fmt:message key="invalid.phoneNumber"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mt-2">
                <div class="col-6">
                    <div class="form-group mt-2">
                        <label for="password" class="form-label"><fmt:message key="password.label"/></label>
                        <input type="password" id="password" name="password" class="form-control"
                               placeholder="<fmt:message key="placeholder.password"/>"
                               pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required/>
                        <div class="invalid-feedback">
                            <fmt:message key="invalid.password"/>
                        </div>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-group mt-2">
                        <label for="passwordRepeat" class="form-label"><fmt:message key="repeatPassword.label"/></label>
                        <input type="password" id="passwordRepeat" name="password_repeat" class="form-control"
                               placeholder="<fmt:message key="placeholder.repeatPassword"/>"
                               pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required/>
                        <div class="invalid-feedback">
                            <fmt:message key="invalid.password"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group mt-3">
                <button type="submit" class="btn btn-dark w-100"><fmt:message key="button.registration"/></button>
                <div class="mt-2">
                <div class="mt-2 d-flex justify-content-center">
                    <a href="<c:url value="/cafe?command=to_login"/>"><fmt:message key="link.login"/></a>
                </div>
                </div>
            </div>
        </form>
    </div>
</body>
</html>