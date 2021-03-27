package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Review;
import com.epam.jwd.cafe.service.ReviewService;
import com.epam.jwd.cafe.util.PaginationContext;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToReviewCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(ToReviewCommand.class);
    private static final ReviewService REVIEW_SERVICE = ReviewService.INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        ResponseContext responseContext;
        try {
            int page = Integer.parseInt(request.getRequestParameters().get(RequestConstant.CURRENT_PAGE));
            List<Review> reviews = REVIEW_SERVICE.findAllReviews();
            if (reviews.size() > 0) {
                PaginationContext<Review> paginationContext = new PaginationContext<>(reviews, page);
                requestMap.put(RequestConstant.PAGINATION_CONTEXT, paginationContext);
            }
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.REVIEW_PAGE), requestMap, new HashMap<>());
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error("Failed move to reviews", e);
            responseContext = new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
        }
        return responseContext;
    }
}
