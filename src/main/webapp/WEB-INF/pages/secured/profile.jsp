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
                    <div class="row m-l-0 m-r-0">
                        <div class="col-sm-4 bg-c-lite-green user-profile">
                            <div class="card-block text-center text-white">
                                <div class="m-b-25"><img src="https://img.icons8.com/bubbles/100/000000/user.png"
                                                         class="img-radius" alt="User-Profile-Image"></div>
                                <h6 class="f-w-600">Hembo Tingor</h6>
                                <p>Web Designer</p> <i
                                    class=" mdi mdi-square-edit-outline feather icon-edit m-t-10 f-16"></i>
                                <button data-bs-toggle="modal" data-bs-target="#modal" class="btn btn-primary mt-2" type="button">
                                    <i class="fa fa-pencil-square-o"></i>
                                </button>
                            </div>
                        </div>
                        <div class="col-sm-8">
                            <div class="card-block">
                                <h6 class="m-b-20 p-b-5 b-b-default f-w-600">Information</h6>
                                <div class="row">
                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600">Email</p>
                                        <h6 class="text-muted f-w-400">rntng@gmail.com</h6>
                                    </div>
                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600">Phone</p>
                                        <h6 class="text-muted f-w-400">98979989898</h6>
                                    </div>
                                </div>
                                <h6 class="m-b-20 m-t-40 p-b-5 b-b-default f-w-600">Projects</h6>
                                <div class="row">
                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600">Recent</p>
                                        <h6 class="text-muted f-w-400">Sam Disuja</h6>
                                    </div>
                                    <div class="col-sm-6">
                                        <p class="m-b-10 f-w-600">Most Viewed</p>
                                        <h6 class="text-muted f-w-400">Dinoter husainm</h6>
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
                    <p id="violations" class="text-danger"></p>
                    <p id="server_message" class="text-danger"></p>
                    <input type="hidden" name="command" value="user-edit-profile">
                    <div class="row mt-3">
                        <div class="col-6">
                            <label for="first-name" class="form-label"><fmt:message key="label.firstName"/></label>
                            <input type="text" id="first-name" name="first_name" class="form-control"
                                   value='dada' pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$" required/>
                            <div class="invalid-feedback">
                                <fmt:message key="error.firstName"/>
                            </div>
                        </div>
                        <div class="col-6">
                            <label for="last-name" class="form-label"><fmt:message key="label.lastName"/></label>
                            <input type="text" id="last-name" name="last_name" class="form-control"
                                   value='$dada2' pattern="^[A-Za-zА-Яа-яЁё']{2,20}?$" required/>
                            <div class="invalid-feedback">
                                <fmt:message key="error.lastName"/>
                            </div>
                        </div>
                    </div>
                    <label for="number" class="form-label mt-4"><fmt:message key="label.phoneNumber"/></label>
                    <div class="input-group has-validation">
                        <input type="text" id="number" name="number" class="form-control" value='+375333132549'
                               pattern="^\+375((44)|(33)|(29))[0-9]{7}$" required/>
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
</div>

<c:import url="../../parts/footer.jsp"/>
</body>
</html>
