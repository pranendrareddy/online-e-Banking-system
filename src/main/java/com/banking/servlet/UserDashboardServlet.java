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
import java.util.List;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
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

        User user = (User) session.getAttribute("user");
        List<Account> accounts = bankingService.getAccountsByUserId(user.getUserId());
        
        request.setAttribute("accounts", accounts);
        request.getRequestDispatcher("/pages/user-dashboard.jsp").forward(request, response);
    }
}
