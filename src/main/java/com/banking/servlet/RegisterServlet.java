package com.banking.servlet;

import com.banking.model.User;
import com.banking.service.BankingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private BankingService bankingService;

    @Override
    public void init() throws ServletException {
        bankingService = new BankingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        try {
            User user = bankingService.registerUser(username, password, fullName, email, phone);
            
            if (user != null) {
                request.setAttribute("success", "Registration successful! Please login.");
                request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        }
    }
}
