<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Добавить цель - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<c:import url="/WEB-INF/views/layout/header.jsp"/>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-6">
            <div class="card">
                <div class="card-header">
                    <h4 class="card-title mb-0">Добавить личную цель</h4>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/goals">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" name="goalType" value="personal">

                        <div class="mb-3">
                            <label for="name" class="form-label">Название цели</label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>

                        <div class="mb-3">
                            <label for="targetAmount" class="form-label">Целевая сумма (₽)</label>
                            <input type="number" class="form-control" id="targetAmount" name="targetAmount"
                                   min="0.01" step="0.01" required>
                        </div>

                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/goals" class="btn btn-secondary">Отмена</a>
                            <button type="submit" class="btn btn-primary">Создать цель</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>