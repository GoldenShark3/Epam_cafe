package com.epam.jwd.cafe.command.impl;

import com.epam.jwd.cafe.command.Command;
import com.epam.jwd.cafe.command.CommandManager;
import com.epam.jwd.cafe.command.ForwardResponseType;
import com.epam.jwd.cafe.command.RequestContext;
import com.epam.jwd.cafe.command.ResponseContext;
import com.epam.jwd.cafe.command.RestResponseType;
import com.epam.jwd.cafe.command.constant.PageConstant;
import com.epam.jwd.cafe.command.constant.RequestConstant;
import com.epam.jwd.cafe.command.marker.UserCommand;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.handler.Handler;
import com.epam.jwd.cafe.handler.impl.NumberHandler;
import com.epam.jwd.cafe.handler.impl.ReviewHandler;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.Review;
import com.epam.jwd.cafe.model.User;
import com.epam.jwd.cafe.model.dto.UserDto;
import com.epam.jwd.cafe.service.ReviewService;
import com.epam.jwd.cafe.service.UserService;
import com.epam.jwd.cafe.util.LocalizationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The class provides for adding new {@link Review}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class AddReviewCommand implements Command, UserCommand {
    private static final Logger LOGGER = LogManager.getLogger(AddReviewCommand.class);
    private static final ReviewService REVIEW_SERVICE = ReviewService.INSTANCE;
    private static final UserService USER_SERVICE = UserService.INSTANCE;
    private static final Handler REVIEW_HANDLER = new ReviewHandler();

    @Override
    public ResponseContext execute(RequestContext request) {
        Map<String, Object> requestMap = new HashMap<>();
        String rate = request.getRequestParameters().get(RequestConstant.RATE);
        Set<String> errorMessages = REVIEW_HANDLER.handleRequest(request);

        if (errorMessages.isEmpty()) {
            String feedback = request.getRequestParameters().get(RequestConstant.REVIEW);
            int reviewRate = Integer.parseInt(rate);
            UserDto userDto = (UserDto) request.getSessionAttributes().get(RequestConstant.USER);

            try {
                Optional<User> userOptional = USER_SERVICE.findById(userDto.getId());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    Review review = Review.builder()
                            .withId(user.getId())
                            .withFeedback(feedback)
                            .withRate(reviewRate)
                            .withUser(user).build();

                    REVIEW_SERVICE.createReview(review);
                    requestMap.put(RequestConstant.REDIRECT_COMMAND, CommandManager.TO_REVIEW.getCommandName());
                } else {
                    requestMap.put(RequestConstant.SERVER_MESSAGE,
                            LocalizationMessage.localize(request.getLocale(), "serverMessage.userNotFound"));
                }
                return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
            } catch (ServiceException e) {
                LOGGER.error("Failed to add review", e);
                return new ResponseContext(new ForwardResponseType(PageConstant.ERROR_PAGE), requestMap, new HashMap<>());
            }
        } else {
            requestMap.put(RequestConstant.ERROR_MESSAGE, errorMessages);
            return new ResponseContext(new RestResponseType(), requestMap, new HashMap<>());
        }
    }
}
