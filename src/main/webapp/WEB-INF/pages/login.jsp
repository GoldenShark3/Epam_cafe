<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="ru_RU"/>
<fmt:setBundle basename="content_ru_RU"/>

<!DOCTYPE html>
<html>
<head>
    <c:import url="../parts/header.jsp"/>
    <title><fmt:message key="login.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
        <%@include file='../styles/login_style.css' %>
    </style>
</head>
<body>
<c:import url="../parts/navbar.jsp"/>

<div class="wrapper fadeInDown">
    <div id="formContent">
        <div class="fadeIn first">
            <img src="../../img/LOGO (2).png" id="icon" alt="User Icon"/>
        </div>
        <form>
            <div class="input-container">
                <i class="fa fa-envelope icon"></i>
                <input class="input-field" type="text" placeholder="example@mail.ru" name="email" required>
            </div>
            <div class="input-container">
                <i class="fa fa-key icon"></i>
                <input class="input-field" type="password" placeholder="Password" name="password" required>
            </div>
            <button type="submit" class="fadeIn fourth"><fmt:message key="button.login"/></button>
        </form>

        <div id="formFooter">
            <a class="underlineHover" href="#"><fmt:message key="link.registration"/></a>
        </div>
    </div>
</div>
<c:import url="../parts/footer.jsp"/>
</body>
</html>