package com.banking.servlet;

import com.banking.model.User;
import com.banking.service.BankingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private BankingService bankingService;

    @Override
    public void init() throws ServletException {
        bankingService = new BankingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = bankingService.login(username, password);
            
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                
                // Redirect based on role
                if ("ADMIN".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/user/dashboard");
                }
            } else {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        }
    }
}
