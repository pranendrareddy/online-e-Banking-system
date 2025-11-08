<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.banking.model.User, com.banking.model.Account, com.banking.model.Transaction, java.util.List, java.math.BigDecimal" %>
<%
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"ADMIN".equals(admin.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    int totalUsers = (int) request.getAttribute("totalUsers");
    int totalAccounts = (int) request.getAttribute("totalAccounts");
    int totalTransactions = (int) request.getAttribute("totalTransactions");
    BigDecimal totalBalance = (BigDecimal) request.getAttribute("totalBalance");
    List<User> users = (List<User>) request.getAttribute("users");
    List<Account> accounts = (List<Account>) request.getAttribute("accounts");
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Smart Banking System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar navbar-admin">
        <div class="nav-container">
            <h1>Admin Dashboard</h1>
            <div class="nav-menu">
                <span>Welcome, <%= admin.getFullName() %></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="dashboard-container">
        <div class="stats-grid">
            <div class="stat-card stat-users">
                <h3><%= totalUsers %></h3>
                <p>Total Users</p>
            </div>
            <div class="stat-card stat-accounts">
                <h3><%= totalAccounts %></h3>
                <p>Total Accounts</p>
            </div>
            <div class="stat-card stat-transactions">
                <h3><%= totalTransactions %></h3>
                <p>Total Transactions</p>
            </div>
            <div class="stat-card stat-balance">
                <h3>$<%= String.format("%.2f", totalBalance) %></h3>
                <p>Total Balance</p>
            </div>
        </div>
        
        <div class="section">
            <h2>All Users</h2>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Role</th>
                            <th>Created At</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (User user : users) { %>
                            <tr>
                                <td><%= user.getUserId() %></td>
                                <td><%= user.getUsername() %></td>
                                <td><%= user.getFullName() %></td>
                                <td><%= user.getEmail() %></td>
                                <td><%= user.getPhone() %></td>
                                <td><span class="badge <%= user.getRole().toLowerCase() %>">
                                    <%= user.getRole() %>
                                </span></td>
                                <td><%= user.getCreatedAt() %></td>
                                <td><span class="badge <%= user.isActive() ? "active" : "inactive" %>">
                                    <%= user.isActive() ? "ACTIVE" : "INACTIVE" %>
                                </span></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="section">
            <h2>All Accounts</h2>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Account Number</th>
                            <th>Type</th>
                            <th>Balance</th>
                            <th>Status</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Account account : accounts) { %>
                            <tr>
                                <td><%= account.getAccountId() %></td>
                                <td><%= account.getAccountNumber() %></td>
                                <td><%= account.getAccountType() %></td>
                                <td>$<%= String.format("%.2f", account.getBalance()) %></td>
                                <td><span class="badge <%= account.getStatus().toLowerCase() %>">
                                    <%= account.getStatus() %>
                                </span></td>
                                <td><%= account.getCreatedAt() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="section">
            <h2>Recent Transactions</h2>
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Account ID</th>
                            <th>Type</th>
                            <th>Amount</th>
                            <th>Balance After</th>
                            <th>Description</th>
                            <th>Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                        int displayCount = Math.min(50, transactions.size());
                        for (int i = 0; i < displayCount; i++) {
                            Transaction transaction = transactions.get(i);
                        %>
                            <tr>
                                <td><%= transaction.getTransactionId() %></td>
                                <td><%= transaction.getAccountId() %></td>
                                <td><span class="badge <%= transaction.getTransactionType().toLowerCase() %>">
                                    <%= transaction.getTransactionType() %>
                                </span></td>
                                <td>$<%= String.format("%.2f", transaction.getAmount()) %></td>
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
        </div>
    </div>
</body>
</html>
