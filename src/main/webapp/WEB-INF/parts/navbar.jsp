<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<c:set var="isAuthorized" value="${not empty sessionScope.user}" scope="session"/>
<c:if test="${isAuthorized}">
    <c:set var="user" value="${sessionScope.user}" scope="session"/>
    <c:set var="isAdmin" value="${sessionScope.user.role.name eq 'ADMIN'}" scope="request"/>
</c:if>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid ">

        <a class="navbar-brand" href="<c:url value="/cafe?command=to_main"/>">
            <img src="<c:url value='/img/logo.png'/>" alt="Epam_cafe" width="200" height="40"/>
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse mr-5 row" id="navbarSupportedContent">
            <ul class="navbar-nav mb-2 mb-lg-0 col-9">

                <li class="nav-item">
                    <a class="nav-link active " aria-current="page"
                       href="<c:url value="/cafe?command=to_main"/>"><fmt:message key="link.home"/></a>
                </li>

                <li class="nav-item">
                    <a class="nav-link active" aria-current="page"
                       href="<c:url value='/cafe?command=to_menu'/>"><fmt:message key="link.menu"/></a>
                </li>

                <c:if test="${isAuthorized}">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="<c:url value="/cafe?command=to_profile"/>">
                            <fmt:message key="link.profile"/></a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="<c:url value="/cafe?command=to_my_orders&page=1"/>">
                            <fmt:message key="link.myOrders"/></a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="<c:url value="/cafe?command=to_review&page=1"/>">
                            <fmt:message key="link.review"/></a>
                    </li>

                    <c:if test="${isAdmin}">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="<c:url
                            value="/cafe?command=to_users&page=1"/>">
                                <fmt:message key="link.users"/></a>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="<c:url value="/cafe?command=to_orders&page=1"/>">
                                <fmt:message key="link.orders"/></a>
                        </li>
                    </c:if>
                </c:if>
            </ul>

            <div class="navbar-nav col-3 d-flex align-content-center justify-content-end">

                <c:if test="${isAuthorized}">
                    <a href="<c:url value="/cafe?command=to_cart"/>">
                        <img id="cart" src="<c:url value='/img/clear_cart.png'/>" alt="cart" width="35" height="35"/>
                    </a>
                </c:if>

                <form class="mb-0" style="margin-left: 20px" action="<c:url value="/cafe"/>" method="post">
                    <input type="hidden" name="command" value="locale_switch">
                    <input type="hidden" name="page" value="${ requestScope.page }">

                    <select class="form-select" name="locale" onchange="submit()">
                        <option value="en_US" <c:if test="${locale eq 'en_US'}">selected</c:if>>English</option>
                        <option value="ru_RU" <c:if test="${locale eq 'ru_RU'}">selected</c:if>>Русский</option>
                        <option value="be_BY" <c:if test="${locale eq 'be_BY'}">selected</c:if>>Беларуская</option>
                    </select>
                </form>

                <c:if test="${not isAuthorized}">
                    <form class="mb-0" style="margin-left: 20px" action="<c:url value='/cafe'/>">
                        <input type="hidden" name="command" value="to_login">
                        <button class="btn btn-outline-light"><fmt:message key="button.login"/></button>
                    </form>
                </c:if>

                <c:if test="${isAuthorized}">
                    <form class="mb-0" style="margin-left: 20px" action="<c:url value='/cafe'/>">
                        <input type="hidden" name="command" value="logout">
                        <button id="logout" class="btn btn-outline-light"><fmt:message key="button.logout"/></button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</nav>