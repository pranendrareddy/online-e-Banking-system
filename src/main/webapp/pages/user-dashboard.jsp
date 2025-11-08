<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.banking.model.User, com.banking.model.Account, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Account> accounts = (List<Account>) request.getAttribute("accounts");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Smart Banking System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <h1>Smart Banking System</h1>
            <div class="nav-menu">
                <span>Welcome, <%= user.getFullName() %></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="dashboard-container">
        <% if (session.getAttribute("message") != null) { %>
            <div class="alert alert-success">
                <%= session.getAttribute("message") %>
                <% session.removeAttribute("message"); %>
            </div>
        <% } %>
        
        <% if (session.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                <%= session.getAttribute("error") %>
                <% session.removeAttribute("error"); %>
            </div>
        <% } %>
        
        <div class="section">
            <h2>My Accounts</h2>
            
            <% if (accounts != null && !accounts.isEmpty()) { %>
                <div class="account-grid">
                    <% for (Account account : accounts) { %>
                        <div class="account-card">
                            <div class="account-header">
                                <h3><%= account.getAccountType() %></h3>
                                <span class="account-status <%= account.getStatus().toLowerCase() %>">
                                    <%= account.getStatus() %>
                                </span>
                            </div>
                            <div class="account-number">
                                Account #: <%= account.getAccountNumber() %>
                            </div>
                            <div class="account-balance">
                                $<%= String.format("%.2f", account.getBalance()) %>
                            </div>
                            <div class="account-actions">
                                <button class="btn btn-small btn-primary" onclick="showDepositForm(<%= account.getAccountId() %>)">
                                    Deposit
                                </button>
                                <button class="btn btn-small btn-primary" onclick="showWithdrawForm(<%= account.getAccountId() %>)">
                                    Withdraw
                                </button>
                                <a href="${pageContext.request.contextPath}/user/transaction?accountId=<%= account.getAccountId() %>" 
                                   class="btn btn-small btn-secondary">
                                    View History
                                </a>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <p>No accounts found. Create your first account below.</p>
            <% } %>
            
            <button class="btn btn-primary" onclick="showCreateAccountForm()">Create New Account</button>
        </div>
    </div>
    
    <!-- Create Account Modal -->
    <div id="createAccountModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal('createAccountModal')">&times;</span>
            <h2>Create New Account</h2>
            <form action="${pageContext.request.contextPath}/user/account" method="post">
                <div class="form-group">
                    <label for="accountType">Account Type</label>
                    <select id="accountType" name="accountType" required>
                        <option value="SAVINGS">Savings Account</option>
                        <option value="CURRENT">Current Account</option>
                        <option value="FIXED_DEPOSIT">Fixed Deposit</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="initialBalance">Initial Balance</label>
                    <input type="number" id="initialBalance" name="initialBalance" step="0.01" min="0" value="0" required>
                </div>
                <button type="submit" class="btn btn-primary">Create Account</button>
            </form>
        </div>
    </div>
    
    <!-- Deposit Modal -->
    <div id="depositModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal('depositModal')">&times;</span>
            <h2>Deposit Money</h2>
            <form action="${pageContext.request.contextPath}/user/transaction" method="post">
                <input type="hidden" name="action" value="deposit">
                <input type="hidden" id="depositAccountId" name="accountId">
                <div class="form-group">
                    <label for="depositAmount">Amount</label>
                    <input type="number" id="depositAmount" name="amount" step="0.01" min="0.01" required>
                </div>
                <div class="form-group">
                    <label for="depositDescription">Description (optional)</label>
                    <input type="text" id="depositDescription" name="description">
                </div>
                <button type="submit" class="btn btn-primary">Deposit</button>
            </form>
        </div>
    </div>
    
    <!-- Withdraw Modal -->
    <div id="withdrawModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal('withdrawModal')">&times;</span>
            <h2>Withdraw Money</h2>
            <form action="${pageContext.request.contextPath}/user/transaction" method="post">
                <input type="hidden" name="action" value="withdraw">
                <input type="hidden" id="withdrawAccountId" name="accountId">
                <div class="form-group">
                    <label for="withdrawAmount">Amount</label>
                    <input type="number" id="withdrawAmount" name="amount" step="0.01" min="0.01" required>
                </div>
                <div class="form-group">
                    <label for="withdrawDescription">Description (optional)</label>
                    <input type="text" id="withdrawDescription" name="description">
                </div>
                <button type="submit" class="btn btn-primary">Withdraw</button>
            </form>
        </div>
    </div>
    
    <script>
        function showCreateAccountForm() {
            document.getElementById('createAccountModal').style.display = 'block';
        }
        
        function showDepositForm(accountId) {
            document.getElementById('depositAccountId').value = accountId;
            document.getElementById('depositModal').style.display = 'block';
        }
        
        function showWithdrawForm(accountId) {
            document.getElementById('withdrawAccountId').value = accountId;
            document.getElementById('withdrawModal').style.display = 'block';
        }
        
        function closeModal(modalId) {
            document.getElementById(modalId).style.display = 'none';
        }
        
        // Close modal when clicking outside of it
        window.onclick = function(event) {
            const modals = document.getElementsByClassName('modal');
            for (let modal of modals) {
                if (event.target == modal) {
                    modal.style.display = 'none';
                }
            }
        }
    </script>
</body>
</html>
