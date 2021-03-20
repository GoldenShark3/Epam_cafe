<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<html>
<head>
    <title><fmt:message key="title.profile"/></title>
    <c:import url="../../parts/header.jsp"/>
    <style>
        <%@include file="../../styles/profile_style.css"%>
    </style>
</head>
<body>

<c:import url="../../parts/navbar.jsp"/>

<div class="page-content page-container mx-auto" id="page-content">
    <div class="padding">
        <div class="row container d-flex justify-content-center">
            <div class="col-xl-6 col-md-12">
                <div class="card user-card-full">
                    <div class="row">
                        <div class="col-sm-4 bg-c-lite-green user-profile">
                            <div class="card-block text-center text-white">
                                <div class="m-b-25"><img src="https://img.icons8.com/bubbles/100/000000/user.png"
                                                         class="img-radius" alt="User-Profile-Image"></div>
                                <h6 class="f-w-600">${requestScope.user.firstName} ${requestScope.user.lastName}</h6>
                                <p><b>${requestScope.user.role.name}</b></p>
                                <i class=" mdi mdi-square-edit-outline feather icon-edit m-t-10 f-16"></i>
                                <button data-bs-toggle="modal" data-bs-target="#modal" class="btn btn-primary mt-2"
                                        type="button">
                                    <i class="fa fa-pencil-square-o"></i>
                                </button>
                            </div>
                        </div>

                        <div class="col-sm-8">
                            <div class="card-block">
                                <h5 class="mb-5 p-b-5 b-b-default f-w-600 text-center"><fmt:message
                                        key="label.information"/></h5>
                                <div class="row">
                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600"><fmt:message key="label.email"/></p>
                                        <h6 class="text-muted f-w-400"><fmt:message key="label.phoneNumber"/></h6>
                                    </div>

                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600">${requestScope.user.email}</p>
                                        <h6 class="text-muted f-w-400">${requestScope.user.phoneNumber}</h6>
                                    </div>

                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600"><fmt:message key="label.balance"/></p>
                                        <h6 class="text-muted f-w-400"><fmt:message key="label.loyaltyPoints"/></h6>
                                    </div>

                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600">${requestScope.user.balance}$</p>
                                        <h6 class="text-muted f-w-400">${requestScope.user.loyaltyPoints}</h6>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.edit"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form name="edit" action="<c:url value="/cafe"/>" method="post" class="needs-validation"
                  novalidate accept-charset="UTF-8">

                <div class="modal-body">
                    <p id="error_message" class="text-danger"></p>
                    <p id="server_message" class="text-danger"></p>
                    <input type="hidden" name="command" value="edit_profile">

                    <div class="row mt-3">
                        <div class="col-6">
                            <label for="first-name" class="form-label"><fmt:message key="label.firstName"/></label>
                            <input type="text" id="first-name" name="firstName" class="form-control"
                                   value='${requestScope.user.firstName}' pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$"
                                   required/>
                            <div class="invalid-feedback">
                                <fmt:message key="error.firstName"/>
                            </div>
                        </div>
                        <div class="col-6">
                            <label for="last-name" class="form-label"><fmt:message key="label.lastName"/></label>
                            <input type="text" id="last-name" name="lastName" class="form-control"
                                   value='${requestScope.user.lastName}' pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$"
                                   required/>
                            <div class="invalid-feedback">
                                <fmt:message key="error.lastName"/>
                            </div>
                        </div>
                    </div>
                    <label for="number" class="form-label mt-4"><fmt:message key="label.phoneNumber"/></label>
                    <div class="input-group has-validation">
                        <input type="text" id="number" name="number" class="form-control"
                               value='${requestScope.user.phoneNumber}'
                               pattern="^\+375((44)|(33)|(29)|25)[0-9]{7}$" required/>
                        <div class="invalid-feedback">
                            <fmt:message key="error.phoneNumber"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <fmt:message key="button.close"/>
                    </button>
                    <button type="submit" class="btn btn-dark">
                        <fmt:message key="button.save"/>
                    </button>
                </div>
            </form>
        </div>
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
</div>
<c:import url="../../parts/footer.jsp"/>
</body>
</html>
