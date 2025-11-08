package com.banking.servlet;

import com.banking.model.Account;
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

@WebServlet("/user/account")
public class AccountServlet extends HttpServlet {
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

        User user = (User) session.getAttribute("user");
        String accountType = request.getParameter("accountType");
        String balanceStr = request.getParameter("initialBalance");

        try {
            BigDecimal initialBalance = new BigDecimal(balanceStr);
            Account account = bankingService.createAccount(user.getUserId(), accountType, initialBalance);
            
            if (account != null) {
                session.setAttribute("message", "Account created successfully!");
            } else {
                session.setAttribute("error", "Failed to create account");
            }
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/user/dashboard");
    }
}
