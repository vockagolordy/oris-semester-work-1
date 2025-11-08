<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Внести вклад - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-6">
            <div class="card">
                <div class="card-header">
                    <h4 class="card-title mb-0">Внести вклад в цель</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty goal}">
                        <div class="mb-4">
                            <h5>${goal.name}</h5>
                            <div class="progress mb-2">
                                <div class="progress-bar" role="progressbar"
                                     style="width: ${goal.progressPercentage}%"
                                     aria-valuenow="${goal.progressPercentage}"
                                     aria-valuemin="0"
                                     aria-valuemax="100">
                                        ${goal.progressPercentage}%
                                </div>
                            </div>
                            <div class="d-flex justify-content-between text-muted">
                                <span>Собрано: ${goal.currentAmount} ₽</span>
                                <span>Осталось собрать: ${goal.remainingAmount} ₽</span>
                            </div>
                        </div>

                        <form method="post" action="${pageContext.request.contextPath}/goals">
                            <input type="hidden" name="action" value="contribute">
                            <input type="hidden" name="goalId" value="${goal.id}">

                            <div class="mb-3">
                                <label for="amount" class="form-label">Сумма вклада (₽)</label>
                                <input type="number" class="form-control" id="amount" name="amount"
                                       min="0.01" step="0.01" max="${goal.remainingAmount}"
                                       placeholder="Максимум: ${goal.remainingAmount} ₽" required>
                                <div class="form-text">
                                    Введите сумму, которую хотите внести в эту цель.
                                </div>
                            </div>

                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/goals" class="btn btn-secondary">Отмена</a>
                                <button type="submit" class="btn btn-primary">Внести вклад</button>
                            </div>
                        </form>
                    </c:if>

                    <c:if test="${empty goal}">
                        <div class="alert alert-danger">
                            Цель не найдена или у вас нет доступа к этой цели.
                        </div>
                        <a href="${pageContext.request.contextPath}/goals" class="btn btn-primary">
                            Вернуться к целям
                        </a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>