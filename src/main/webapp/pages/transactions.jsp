<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.banking.model.User, com.banking.model.Account, com.banking.model.Transaction, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Account account = (Account) request.getAttribute("account");
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction History - Smart Banking System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <h1>Smart Banking System</h1>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/user/dashboard" class="btn btn-secondary">Back to Dashboard</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="dashboard-container">
        <div class="section">
            <h2>Transaction History</h2>
            
            <% if (account != null) { %>
                <div class="account-info-box">
                    <p><strong>Account Number:</strong> <%= account.getAccountNumber() %></p>
                    <p><strong>Account Type:</strong> <%= account.getAccountType() %></p>
                    <p><strong>Current Balance:</strong> $<%= String.format("%.2f", account.getBalance()) %></p>
                </div>
            <% } %>
            
            <% if (transactions != null && !transactions.isEmpty()) { %>
                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Balance Before</th>
                                <th>Balance After</th>
                                <th>Description</th>
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Transaction transaction : transactions) { %>
                                <tr>
                                    <td><%= transaction.getTransactionId() %></td>
                                    <td><span class="badge <%= transaction.getTransactionType().toLowerCase() %>">
                                        <%= transaction.getTransactionType() %>
                                    </span></td>
                                    <td>$<%= String.format("%.2f", transaction.getAmount()) %></td>
                                    <td>$<%= String.format("%.2f", transaction.getBalanceBefore()) %></td>
                                    <td>$<%= String.format("%.2f", transaction.getBalanceAfter()) %></td>
                                    <td><%= transaction.getDescription() %></td>
                                    <td><%= transaction.getTransactionDate() %></td>
                                    <td><span class="badge <%= transaction.getStatus().toLowerCase() %>">
                                        <%= transaction.getStatus() %>
                                    </span></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } else { %>
                <p>No transactions found.</p>
            <% } %>
        </div>
    </div>
</body>
</html>
