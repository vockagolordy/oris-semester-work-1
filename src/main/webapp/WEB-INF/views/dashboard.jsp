<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Главная - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <h1>Добро пожаловать, ${currentUser.name}!</h1>

    <div class="row mt-4">
        <div class="col-12 col-md-4 mb-3">
            <div class="card text-white bg-primary h-100">
                <div class="card-body">
                    <h5 class="card-title">Баланс</h5>
                    <h2 class="card-text">${balance} ₽</h2>
                </div>
            </div>
        </div>

        <div class="col-12 col-md-4 mb-3">
            <div class="card text-white bg-success h-100">
                <div class="card-body">
                    <h5 class="card-title">Доходы</h5>
                    <h2 class="card-text">${totalIncome} ₽</h2>
                </div>
            </div>
        </div>

        <div class="col-12 col-md-4 mb-3">
            <div class="card text-white bg-danger h-100">
                <div class="card-body">
                    <h5 class="card-title">Расходы</h5>
                    <h2 class="card-text">${totalExpenses} ₽</h2>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-12 col-lg-6 mb-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="card-title mb-0">Мои цели</h5>
                </div>
                <div class="card-body">
                    <c:if test="${empty personalGoals}">
                        <p class="text-muted">Нет личных целей</p>
                    </c:if>
                    <c:forEach items="${personalGoals}" var="goal">
                        <div class="mb-3">
                            <h6>${goal.name}</h6>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar"
                                     style="width: ${goal.progressPercentage}%"
                                     aria-valuenow="${goal.progressPercentage}"
                                     aria-valuemin="0"
                                     aria-valuemax="100">
                                    <span class="d-none d-sm-inline">${goal.currentAmount} / ${goal.targetAmount} ₽</span>
                                    <span class="d-sm-none">${goal.progressPercentage}%</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="col-12 col-lg-6 mb-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="card-title mb-0">Мои группы</h5>
                </div>
                <div class="card-body">
                    <c:if test="${empty userGroups}">
                        <p class="text-muted">Не состоите в группах</p>
                    </c:if>
                    <c:forEach items="${userGroups}" var="group">
                        <div class="mb-2">
                            <a href="${pageContext.request.contextPath}/groups/${group.id}"
                               class="text-decoration-none">
                                    ${group.name}
                            </a>
                            <c:if test="${group.createdBy == currentUser.id}">
                                <span class="badge bg-success ms-1">Создатель</span>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>