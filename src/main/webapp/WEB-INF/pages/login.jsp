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
    <form name="login" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-25 border-dark"
          novalidate>
        <h2 class="d-flex justify-content-center"><fmt:message key="login.caption"/></h2>

        <p id="server_message" class="text-danger">${server_message}</p>
        <p id="error_message" class="text-danger">${error_message}</p>
        <input type="hidden" name="command" value="login">

        <div>
            <label class="form-label" for="username"><fmt:message key="username.label"/></label>
            <input type="text" class="form-control" placeholder="<fmt:message key="placeholder.username"/>" name="username"
                   id="username" pattern="^[(\w)-]{4,20}"
                   required/>
            <div class="invalid-feedback">
                <fmt:message key="invalid.username"/>
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
