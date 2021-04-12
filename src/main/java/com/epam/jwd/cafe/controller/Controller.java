package com.epam.jwd.cafe.controller;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RedirectResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.ResponseType;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The class provide entry point for all requests
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */

@WebServlet(urlPatterns = {"/cafe", "*.do"})
@MultipartConfig(location = "C:\\Users\\Aleksey\\Desktop\\EPAM\\EpamCafe\\out\\artifacts\\epam_cafe_war_exploded\\data",
        maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 2)
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
        String commandName = req.getParameter(RequestConstant.COMMAND);
        Command command = Command.of(commandName);

        ResponseContext responseContext = command.execute(requestContext);
        responseContext.getRequestAttributes().forEach(req::setAttribute);
        responseContext.getSessionAttributes().forEach(req.getSession()::setAttribute);

        if (responseContext.getRequestAttributes().containsKey(RequestConstant.LOGOUT)) {
            req.getSession().invalidate();
            responseContext.getRequestAttributes().remove(RequestConstant.LOGOUT);
        }
        chooseResponseType(req, resp, responseContext);
    }

    private void chooseResponseType(HttpServletRequest req, HttpServletResponse resp, ResponseContext responseContext) throws IOException, ServletException {
        ResponseType responseType = responseContext.getResponseType();

        switch (responseType.getType()) {
            case REDIRECT:
                resp.sendRedirect(req.getContextPath() + ((RedirectResponseType) responseType).getCommand());
                break;
            case FORWARD:
                req.getRequestDispatcher(((ForwardResponseType) responseType).getPage()).forward(req, resp);
                break;
            case REST:
                resp.getWriter().write(new ObjectMapper().writeValueAsString(responseContext.getRequestAttributes()));
                break;
        }
    }
}
