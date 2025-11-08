<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Smart Banking System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <div class="error-container">
            <h1>Oops! Something went wrong</h1>
            <p class="error-message">
                <% if (exception != null) { %>
                    <%= exception.getMessage() %>
                <% } else { %>
                    An error occurred while processing your request.
                <% } %>
            </p>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Go to Login</a>
        </div>
    </div>
</body>
</html>
