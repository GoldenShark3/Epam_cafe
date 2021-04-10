<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<html>
<head>
    <title><fmt:message key="title.addProductType"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>
<div class="container-fluid d-flex justify-content-center mt-4">
    <form name="addType" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-50" novalidate
          enctype="multipart/form-data">
        <p class="display-6 text-center"><fmt:message key="title.addProductType"/></p>

        <p id="error_message" class="text-danger"></p>
        <p id="server_message" class="text-danger"></p>
        <input type="hidden" name="command" value="add_product_type">

        <div class="form-group mt-4">
            <label for='type_name' class="form-label"><fmt:message key="label.productTypeName"/></label>
            <input id="type_name" class="form-control" type="text" name="product_name"
                   placeholder="<fmt:message key="label.productTypeName"/>" pattern="^[A-Za-zа-яА-Я\s'-]{2,20}?$"
                   required>
            <div class="invalid-feedback">
                <fmt:message key="error.productTypeName"/>
            </div>
        </div>
        <div class="form-group mt-4">
            <label for="img" class="form-label"><fmt:message key="label.imgFile"/></label>
            <input id="img" class="form-control" type="file" name="img_file" accept="image/x-png,image/jpeg"
                   required>
        </div>
        <button type="submit" class="btn btn-dark mt-3 w-100"><fmt:message key="button.create"/></button>
    </form>
</div>
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

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
