<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>
<html>
<head>
    <title><fmt:message key="title.createOrder"/></title>
    <c:import url="../../parts/header.jsp"/>
</head>
<body>
<c:import url="../../parts/navbar.jsp"/>

<div class="container-fluid d-flex justify-content-center mt-4">
    <form name="createOrder" action="<c:url value="/cafe"/>" method="post" class="needs-validation w-80" novalidate
          enctype="multipart/form-data">

        <p class="display-3 text-center"><fmt:message key="title.createOrder"/></p>
        <p id="error_message" class="text-danger"></p>
        <p id="server_message" class="text-danger"></p>
        <input type="hidden" name="command" value="create_order">

        <div class="form-group mt-4">
            <label for='delivery_address' class="form-label"><fmt:message key="label.deliveryAddress"/></label>
            <input id="delivery_address" class="form-control" type="text" name="delivery_address"
                   placeholder="<fmt:message key="placeholder.deliveryAddress"/>" pattern="^.{2,50}?$" required>
            <div class="invalid-feedback">
                <fmt:message key="error.deliveryAddress"/>
            </div>
        </div>

        <div class="row mt-4">
            <div class="form-group col-6">
                <label for="select_method" class="form-label"><fmt:message key="label.paymentMethod"/></label>
                <select id="select_method" name="payment_method" class="form-select">
                    <option value="BALANCE" selected><fmt:message key="paymentMethod.balance"/></option>
                    <option value="CASH"><fmt:message key="paymentMethod.cash"/></option>
                    <option value="CARD"><fmt:message key="paymentMethod.card"/></option>
                </select>
            </div>

            <div class="form-group col-6">
                <label for="delivery_date" class="form-label"><fmt:message key="label.deliveryDate"/></label>
                <input id="delivery_date" type="datetime-local" class="form-control" name="delivery_date" required>
            </div>

        </div>

        <div class="form-group mt-4">
            <label for="submit_button" class="text-success" style="font-size: 20px">
                <fmt:message key="label.totalCost"/>: ${requestScope.total_cost}$
            </label>
            <button type="submit" id="submit_button" class="btn btn-dark w-100">
                <fmt:message key="button.createOrder"/>
            </button>
        </div>
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

        alert("<fmt:message key="info.successfullyCreateOrder"/>");

        let redirectCommand = parse.redirect_command;
        if (redirectCommand != null) {
            window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
        }
    }
</script>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
