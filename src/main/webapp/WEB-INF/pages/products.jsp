<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="app_tag" uri="/WEB-INF/taglib/pagination.tld" %>

<c:set var="locale" value="${not empty sessionScope.locale ? sessionScope.locale : 'ru_RU'}"/>
<fmt:setBundle basename="content"/>
<fmt:setLocale value="${locale}" scope="session"/>

<html>
<head>
    <title><fmt:message key="title.products"/></title>
    <c:import url="../parts/header.jsp"/>
</head>
<body>
<c:import url="../parts/navbar.jsp"/>

<c:if test="${requestScope.containsKey('product_type')}">
    <div class="d-flex justify-content-center mt-0">
        <p class="display-1">
                ${requestScope.product_type.name}
        </p>
    </div>
</c:if>

<c:if test="${not requestScope.containsKey('product_type')}">
    <div class="d-flex justify-content-center mt-0">
        <p class="display-1">
            <fmt:message key="title.emptyProducts"/>
        </p>
    </div>
</c:if>

<div class="row justify-content-center">
    <c:forEach items="${requestScope.pagination_context.objectList}" var="item">
        <div class="card text-center m-5 col-6" style="width: 18rem;">
            <img src="<c:url value='/data/${item.imgFileName}'/>" class="card-img-top"
                 style="max-height: 18rem;"
                 alt="Item">
            <div class="card-body row align-items-end">
                <h5 class="card-title">${item.name}</h5>
                <p class="card-text">${item.description}</p>
                <p class="card-text">${item.price}$</p>

                <c:if test="${isAuthorized}">
                    <form name="addToCart-${item.id}" method="post" action="<c:url value="/cafe"/>"
                          class="needs-validation w-100 p-0 m-0" novalidate>
                        <input type="hidden" name="id" value="${item.id}">
                        <input type="hidden" name="command" value="add_to_cart">
                        <button type="submit" onclick="added(this)" class="btn btn-dark w-100">
                            <fmt:message key="button.add"/>
                        </button>
                    </form>
                </c:if>

                <c:if test="${isAdmin}">
                    <button type="button" class="btn btn-outline-secondary w-100 mt-3" data-bs-toggle="modal"
                            data-bs-target="#exampleModal"
                            onclick='prepareModal("${item.id}", "${item.name}", "${item.description}", "${item.price}")'>
                        <fmt:message key="button.edit"/>
                    </button>
                </c:if>

            </div>
        </div>
    </c:forEach>

    <c:if test="${isAdmin}">
        <c:if test="${(param.page eq requestScope.pagination_context.getTotalPages()) or (not requestScope.containsKey('product_type'))}">
            <div class="card text-center m-5 col-6" style="width: 18rem;">
                <img src="<c:url value="../../img/add.svg"/>" class="card-img-top mt-1" style="max-height: 18rem;"
                     alt="item">
                <div class="card-body row align-items-end">
                    <a href="<c:url value='/cafe?command=to_add_product&type_id=${param.type_id}'/>"
                       class="btn btn-dark w-100"><fmt:message key="button.addProduct"/></a>
                </div>
            </div>
        </c:if>

        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="title.edit"/></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">
                        <form name="editProduct" action="<c:url value="/cafe"/>" method="post" class="needs-validation"
                              novalidate enctype="multipart/form-data">

                            <p id="error_message" class="text-danger"></p>
                            <p id="server_message" class="text-danger"></p>
                            <input type="hidden" name="command" value='edit_product'>
                            <input id="id" hidden type="text" name="id" value="">

                            <div class="row mt-2">
                                <div class="form-group col-6">
                                    <label for='product_name' class="form-label">
                                        <fmt:message key="label.productName"/>
                                    </label>
                                    <input id="product_name" class="form-control" type="text" name="product_name"
                                           placeholder="<fmt:message key="placeholder.productName"/>"
                                           pattern="^[A-Za-zа-яА-Я\s'-]{2,20}?$"
                                           required>
                                    <div class="invalid-feedback">
                                        <fmt:message key="error.productTypeName"/>
                                    </div>
                                </div>

                                <div class="form-group col-6">
                                    <label for='product_price' class="form-label"><fmt:message
                                            key="label.price"/></label>
                                    <input id="product_price" class="form-control" type="text" name="product_price"
                                           pattern="^([0-9]{1,3}\.[0-9]{1,2}|[0-9]{1,2})$"
                                           placeholder="<fmt:message key="placeholder.price"/>"
                                           required>
                                    <div class="invalid-feedback">
                                        <fmt:message key="error.price"/>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group mt-4">
                                <label for='product_description' class="form-label"><fmt:message
                                        key="label.description"/></label>
                                <input id="product_description" class="form-control" type="text"
                                       name="product_description"
                                       placeholder="<fmt:message key="placeholder.description"/>" pattern="^.{4,40}$"
                                       required>
                                <div class="invalid-feedback">
                                    <fmt:message key="error.description"/>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="modal-footer">
                        <form name="deleteProduct" action="<c:url value="/cafe"/>" method="post"
                              class="needs-validation"
                              novalidate>
                            <input type="hidden" name="command" value="delete_product" required>
                            <input id="deleteId" type="hidden" name="id" value="" required>
                            <button type="submit" class="btn btn-danger">
                                <fmt:message key="button.delete"/>
                            </button>
                        </form>
                        <button onclick="triggerForm()" class="btn btn-dark">
                            <fmt:message key="button.save"/>
                        </button>
                    </div>

                </div>
            </div>
        </div>

        <script>
            function prepareModal(id, name, description, price) {
                document.getElementById('id').value = id;
                document.getElementById('deleteId').value = id;
                document.getElementById('product_name').value = name;
                document.getElementById('product_description').value = description;
                document.getElementById('product_price').value = price;
            }

            function triggerForm() {
                const htmlFormElement = document.forms.namedItem('editProduct');
                if (htmlFormElement.checkValidity()) {
                    sendRequest(htmlFormElement.name);
                }
                htmlFormElement.classList.add('was-validated');
            }

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
                        + '&type_id=' + '${param.type_id}' + '&page=' + '${param.page}'
                }
            }
        </script>
    </c:if>
</div>

<div class="d-flex justify-content-center m-4">
    <ul class="pagination">
        <app_tag:pagination pages="${requestScope.pagination_context.totalPages}"
                            page="${requestScope.pagination_context.page}"
                            url="/cafe?command=to_menu_item&type_id=${requestScope.product_type.id}&page="/>
    </ul>
</div>

<c:if test="${isAuthorized}">
    <script>
        function added(btn) {
            btn.classList.remove("btn-dark");
            btn.classList.add("btn-success");
            btn.innerText = '<fmt:message key="button.add"/>';
            document.getElementById("cart").src = '<c:url value="../../img/full_cart.png"/>';
            setTimeout(repaintButton, 1000);

            function repaintButton() {
                btn.classList.remove("btn-success");
                btn.classList.add("btn-dark");
                btn.innerText = '<fmt:message key="button.add"/>';
            }
        }

        <c:if test="${not isAdmin}">
        function onAjaxSuccess(data) {
            let parse = JSON.parse(data);
            let redirectCommand = parse.redirect_command;
            if (redirectCommand != null) {
                localStorage.clear();
                window.location.href = '<c:url value="/cafe"/>' + "?command=" + redirectCommand;
            }
        }
        </c:if>
    </script>
</c:if>
<c:import url="../parts/footer.jsp"/>
</body>
</html>
