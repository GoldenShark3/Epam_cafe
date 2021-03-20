package com.epam.jwd.cafe.handler.impl;

import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.handler.AbstractHandler;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.util.LocalizationMessage;
import com.mysql.cj.util.StringUtils;
import javax.servlet.http.Part;
import java.util.HashSet;
import java.util.Set;

public class ImgFileHandler extends AbstractHandler {

    public ImgFileHandler(Handler nextHandler) {
        super(nextHandler);
    }

    public ImgFileHandler() {
    }

    @Override
    public Set<String> handleRequest(RequestContext requestContext) {
        Set<String> errorMessage = new HashSet<>();
        Part imgFile = requestContext.getRequestParts().get(RequestConstant.IMG_FILE);

        if (imgFile == null || StringUtils.isNullOrEmpty(imgFile.getSubmittedFileName())
              || (!imgFile.getSubmittedFileName().endsWith(".png") && !imgFile.getSubmittedFileName().endsWith(".jpg"))){

            errorMessage.add(LocalizationMessage.localize(requestContext.getLocale(), "serverMessage.incorrectImg"));
        }
        if (nextHandler != null) {
            errorMessage.addAll(nextHandler.handleRequest(requestContext));
        }
        return errorMessage;
    }
}
