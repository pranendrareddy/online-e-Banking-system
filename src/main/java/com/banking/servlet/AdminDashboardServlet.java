package com.banking.servlet;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.service.BankingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private BankingService bankingService;

    @Override
    public void init() throws ServletException {
        bankingService = new BankingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User admin = (User) session.getAttribute("user");
        if (!"ADMIN".equals(admin.getRole())) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }

        List<User> users = bankingService.getAllUsers();
        List<Account> accounts = bankingService.getAllAccounts();
        List<Transaction> transactions = bankingService.getAllTransactions();

        // Calculate statistics
        int totalUsers = users.size();
        int totalAccounts = accounts.size();
        int totalTransactions = transactions.size();
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalAccounts", totalAccounts);
        request.setAttribute("totalTransactions", totalTransactions);
        request.setAttribute("totalBalance", totalBalance);
        request.setAttribute("users", users);
        request.setAttribute("accounts", accounts);
        request.setAttribute("transactions", transactions);

        request.getRequestDispatcher("/pages/admin-dashboard.jsp").forward(request, response);
    }
}
