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

@WebServlet("/user/transaction")
public class TransactionServlet extends HttpServlet {
    private BankingService bankingService;

    @Override
    public void init() throws ServletException {
        bankingService = new BankingService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        String accountIdStr = request.getParameter("accountId");
        String amountStr = request.getParameter("amount");
        String description = request.getParameter("description");

        try {
            int accountId = Integer.parseInt(accountIdStr);
            BigDecimal amount = new BigDecimal(amountStr);

            if ("deposit".equals(action)) {
                bankingService.deposit(accountId, amount, description);
                session.setAttribute("message", "Deposit successful!");
            } else if ("withdraw".equals(action)) {
                bankingService.withdraw(accountId, amount, description);
                session.setAttribute("message", "Withdrawal successful!");
            }
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/user/dashboard");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accountIdStr = request.getParameter("accountId");
        
        if (accountIdStr != null) {
            try {
                int accountId = Integer.parseInt(accountIdStr);
                Account account = bankingService.getAccountById(accountId);
                List<Transaction> transactions = bankingService.getTransactionHistory(accountId);
                
                request.setAttribute("account", account);
                request.setAttribute("transactions", transactions);
                request.getRequestDispatcher("/pages/transactions.jsp").forward(request, response);
            } catch (Exception e) {
                session.setAttribute("error", e.getMessage());
                response.sendRedirect(request.getContextPath() + "/user/dashboard");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        }
    }
}
