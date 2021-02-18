package com.epam.jwd.cafe.controller;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RedirectResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.ResponseType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cafe")
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestContext requestContext = new RequestContext(req);
        String commandName = req.getParameter("command");
        Command command = Command.of(commandName);
        ResponseContext responseContext = command.execute(requestContext);
        responseContext.getRequestAttributes().forEach(req::setAttribute);
        responseContext.getSessionAttributes().forEach(req.getSession()::setAttribute);

        ResponseType responseType = responseContext.getResponseType();
        if (responseType.getType().equals(ResponseType.Type.REDIRECT)) {
            resp.sendRedirect(req.getContextPath() + ((RedirectResponseType) responseType).getCommand());
        } else {
            req.getRequestDispatcher(((ForwardResponseType) responseType).getPage()).forward(req, resp);
        }
    }
}
