<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<html>
<head>
    <title><fmt:message key="title.addProduct"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>

<div class="container-fluid d-flex justify-content-center mt-4">
    <form name="addProduct" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-50" novalidate
          enctype="multipart/form-data">

        <p class="display-6 text-center"><fmt:message key="title.addProduct"/></p>
        <p id="error_message" class="text-danger"></p>
        <p id="server_message" class="text-danger"></p>
        <input type="hidden" name="command" value="add_product">
        <input type="text" hidden id="type_id" name="type_id" value="${param.type_id}">

        <div class="row mt-4">
            <div class="form-group col-6">
                <label for='product_name' class="form-label"><fmt:message key="label.productName"/></label>
                <input id="product_name" class="form-control" type="text" name="product_name"
                       placeholder="<fmt:message key="placeholder.productName"/>" pattern="^[A-Za-zа-яА-Я\s'-]{2,20}?$"
                       required>
                <div class="invalid-feedback">
                    <fmt:message key="error.productName"/>
                </div>
            </div>

            <div class="form-group col-6">
                <label for='product_price' class="form-label"><fmt:message key="label.price"/></label>
                <input id="product_price" class="form-control" type="text" name="product_price"
                       placeholder="<fmt:message key="placeholder.price"/>"
                       pattern="^([0-9]{1,3}\.[0-9]{1,2}|[0-9]{1,2})$"
                       required>
                <div class="invalid-feedback">
                    <fmt:message key="error.price"/>
                </div>
            </div>
        </div>

        <div class="form-group mt-4">
            <label for='product_description' class="form-label"><fmt:message key="label.description"/></label>
            <input id="product_description" class="form-control" type="text" name="product_description"
                   placeholder="<fmt:message key="placeholder.description"/>" pattern="^.{4,80}$"
                   required>
            <div class="invalid-feedback">
                <fmt:message key="error.description"/>
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
            window.location.href = '<c:url value="/cafe"/>' + "?command="
                + redirectCommand + '&type_id=${param.type_id}' + '&page=1';
        }
    }
</script>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
