<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setLocale value="${locale}" scope="session"/>
<fmt:setBundle basename="content"/>

<html>
<head>
    <title><fmt:message key="title.main"/></title>
    <c:import url="../parts/header.jsp"/>
</head>
<body >
<c:import url="../parts/navbar.jsp"/>

<div id="carouselExampleCaptions" class="carousel slide" data-ride="carousel">
    <ol class="carousel-indicators">
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active"></li>
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1"></li>
        <li data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="../../img/main_bg_1.png" style="height: 650px" class="d-block w-100" alt="main_welcome">
            <div class="carousel-caption d-none d-md-block">
                <div class="d-flex justify-content-center row">
                    <div class="col-4" style="backdrop-filter: blur(2px); border-radius: 15px;">
                        <h2><fmt:message key="title.main"/></h2>
                        <p style="font-size: 16px;"><fmt:message key="label.main.cafe"/></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="carousel-item">
            <img src="../../img/main_bg_2.png" style="height: 650px" class="d-block w-100" alt="main_burger">
            <div class="carousel-caption d-none d-md-block">
                <div class="d-flex justify-content-center row">
                    <div class="col-4" style="backdrop-filter: blur(2px); border-radius: 15px;">
                        <h2><fmt:message key="title.main.products"/></h2>
                        <p style="font-size: 16px;"><fmt:message key="label.main.products"/></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="carousel-item">
            <img src="../../img/main_bg_3.png" style="height: 650px" class="d-block w-100" alt="main_coffee">
            <div class="carousel-caption d-none d-md-block">
                <div class="d-flex justify-content-center row">
                    <div class="col-4" style="backdrop-filter: blur(2px); border-radius: 15px;">
                        <h2><fmt:message key="title.main.delivery"/></h2>
                        <p style="font-size: 16px;"><fmt:message key="label.main.delivery"/></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleCaptions" role="button" data-bs-slide="prev">
        <span class="icon-prev" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleCaptions" role="button" data-bs-slide="next">
        <span class="icon-next" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </a>
</div>
    <c:import url="../parts/footer.jsp"/>
</body>
</html>
