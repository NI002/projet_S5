package com.giga.spring.servlet;
import  java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontServlet extends HttpServlet{
    RequestDispatcher defaultDispatcher;
    @Override
    public void init() {
        defaultDispatcher = getServletContext().getNamedDispatcher("default");
    }

    @Override
    public void service(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        // Check if the resource exists 
        boolean resourceExists = getServletContext().getResource(path)!=null;
        if (resourceExists) {
            defaultServe(req, res);
        } else {
            customServe(req,res);
        }
    }

    private void defaultServe(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        defaultDispatcher.forward(req, res);
    }

    private void customServe(HttpServletRequest req, HttpServletResponse res) throws IOException {
       try (PrintWriter out = res.getWriter()) {
        String uri = req.getRequestURI();
        String responseBody = """
        <html>
        <head><title>Resource Not Found</title></head>
        <body>
        <h1>Unknown Resource</h1>
        <p>The requested URL was not found: <strong>%s</strong></p>
        </body>
        </html>
        """ .formatted(uri);
        res.setContentType("text/html; charset=UTF-8");
        out.println(responseBody);
        }
    }

}
