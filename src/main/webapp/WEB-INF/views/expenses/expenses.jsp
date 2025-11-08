<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Финансы - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Мои финансы</h1>
        <a href="${pageContext.request.contextPath}/expenses/add" class="btn btn-primary">
            Добавить операцию
        </a>
    </div>

    <div class="row mb-4">
        <div class="col-12 col-md-4 mb-4">
            <div class="card text-white bg-primary">
                <div class="card-body">
                    <h5 class="card-title">Баланс</h5>
                    <h2 class="card-text">${balance} ₽</h2>
                </div>
            </div>
        </div>
        <div class="col-12 col-md-4 mb-4">
            <div class="card text-white bg-success">
                <div class="card-body">
                    <h5 class="card-title">Доходы</h5>
                    <h2 class="card-text">${totalIncome} ₽</h2>
                </div>
            </div>
        </div>
        <div class="col-12 col-md-4 mb-4">
            <div class="card text-white bg-danger">
                <div class="card-body">
                    <h5 class="card-title">Расходы</h5>
                    <h2 class="card-text">${totalExpenses} ₽</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <h5 class="card-title mb-0">История операций</h5>
        </div>
        <div class="card-body">
            <c:if test="${empty expenses}">
                <p class="text-muted">Нет операций</p>
            </c:if>

            <c:forEach items="${expenses}" var="expense">
                <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                    <div>
                        <h6 class="mb-1">${expense.description}</h6>
                        <small class="text-muted">${expense.category} • ${expense.createdAt}</small>
                    </div>
                    <div class="text-end">
                        <c:choose>
                            <c:when test="${expense.type == 'INCOME'}">
                                <span class="text-success fw-bold">+${expense.amount} ₽</span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-danger fw-bold">-${expense.amount} ₽</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>