<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<!doctype html>
<html>
<head>
    <c:import url="../parts/header.jsp"/>
    <title><fmt:message key="title.registration"/></title>
</head>
<body>
<c:import url="../parts/navbar.jsp"/>

<div class="container-fluid d-flex justify-content-center mt-4">
    <form name="registration" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-30 " novalidate>

        <div class="d-flex justify-content-center mb-0">
            <h2><fmt:message key="title.registration"/></h2>
        </div>

        <p id="server_message" class="text-danger">${server_message}</p>
        <p id="error_message" class="text-danger">${error_message}</p>
        <input type="hidden" name="command" value="registration">

        <div class="row mt-3">
            <div class="col-6">
                <label for="firstNameLabel" class="form-label"><fmt:message key="label.firstName"/></label>
                <input type="text" class="form-control"
                       placeholder="<fmt:message key="placeholder.firstName"/>"
                       name="firstName" id="firstNameLabel" pattern="^[A-Za-zА-Яа-яЁё']{2,20}" required/>
                <div class="invalid-feedback">
                    <fmt:message key="error.firstName"/>
                </div>
            </div>

            <div class="col-6">
                <label for="lastNameLabel" class="form-label"><fmt:message key="label.lastName"/></label>
                <input type="text" class="form-control" placeholder="<fmt:message key="placeholder.lastName"/>"
                       name="lastName" id="lastNameLabel" pattern="^[A-Za-zА-Яа-яЁё']{2,20}" required/>
                <div class="invalid-feedback">
                    <fmt:message key="error.lastName"/>
                </div>
            </div>
        </div>

        <div class="form-group mt-2">
            <label for="username" class="form-label"><fmt:message key="label.username"/></label>
            <input type="text" name="username" id="username" pattern="^[(\w)-]{4,20}" class="form-control"
                   placeholder="<fmt:message key="placeholder.username"/>" required/>
            <div class="invalid-feedback">
                <fmt:message key="error.username"/>
            </div>
        </div>

        <div class="row mt-2">
            <div class="col-6">
                <label for="email" class="form-label"><fmt:message key="label.email"/></label>
                <input type="email" id="email" name="email" class="form-control"
                       placeholder="<fmt:message key="placeholder.email"/>" required/>
                <div class="invalid-feedback">
                    <fmt:message key="error.mail"/>
                </div>
            </div>

            <div class="col-6">
                <label for="number" class="form-label"><fmt:message key="label.phoneNumber"/></label>
                <div class="input-group has-validation">
                    <input type="text" id="number" name="number" class="form-control"
                           pattern="^\+375((44)|(33)|(29)|(25))[0-9]{7}$" value="+375" required/>
                    <div class="invalid-feedback">
                        <fmt:message key="error.phoneNumber"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-2">
            <div class="col-6">
                <div class="form-group mt-2">
                    <label for="password" class="form-label"><fmt:message key="label.password"/></label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="<fmt:message key="placeholder.password"/>"
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required/>
                    <div class="invalid-feedback">
                        <fmt:message key="error.password"/>
                    </div>
                </div>
            </div>

            <div class="col-6">
                <div class="form-group mt-2">
                    <label for="passwordRepeat" class="form-label"><fmt:message key="label.repeatPassword"/></label>
                    <input type="password" id="passwordRepeat" name="password_repeat" class="form-control"
                           placeholder="<fmt:message key="placeholder.repeatPassword"/>"
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}" required/>
                    <div class="invalid-feedback">
                        <fmt:message key="error.password"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group mt-3">
            <button type="submit" class="btn btn-dark w-100"><fmt:message key="button.register"/></button>
            <div class="mt-2">
                <div class="mt-2 d-flex justify-content-center">
                    <a href="<c:url value="/cafe?command=to_login"/>"><fmt:message key="link.login"/></a>
                </div>
            </div>
        </div>
    </form>
    <script>
        function onAjaxSuccess(data) {
            let pMessages = document.getElementById("server_message");
            let eMessages = document.getElementById("error_message");
            pMessages.innerText = "";
            eMessages.innerText = "";

            let parse = JSON.parse(data);

            let serverMessages = parse.server_message;
            let errorMessages = parse.error_message;

            if (serverMessages != null) {
                pMessages.innerText += serverMessages + '\n';
            }

            if (errorMessages != null) {
                for (let i = 0; i < errorMessages.length; i++) {
                    eMessages.innerText += errorMessages[i] + '\n';
                }
            }

            if (serverMessages == null && errorMessages == null) {
                alert("<fmt:message key="info.registrationSuccessfully"/>");
            }

            let redirectCommand = parse.redirect_command;
            if (redirectCommand != null) {
                window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand
            }
        }
    </script>
</div>
    <c:import url="../parts/footer.jsp"/>
</body>
</html>
