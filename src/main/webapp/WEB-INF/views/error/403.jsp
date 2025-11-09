<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Доступ запрещен - Budget Buddy</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center">
            <div class="card shadow">
                <div class="card-body py-5">
                    <h1 class="display-1 text-danger">403</h1>
                    <h2 class="mb-4">Доступ запрещен</h2>
                    <p class="text-muted mb-4">У вас нет прав для доступа к этому ресурсу</p>
                    <div class="d-grid gap-2 d-md-block">
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">На главную</a>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary">Войти с другим аккаунтом</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>