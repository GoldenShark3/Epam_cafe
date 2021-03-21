package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.impl.ReviewDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class ReviewService {
    private final Logger LOGGER = LogManager.getLogger(ProductTypeService.class);
    public static final ReviewService INSTANCE = new ReviewService();
    private static final ReviewDao REVIEW_DAO = ReviewDao.INSTANCE;

    private ReviewService(){
    }

    public List<Review> findAllReviews() throws ServiceException {
        List<Review> reviews;
        try {
            reviews = REVIEW_DAO.findAll();
        } catch (DaoException e) {
            LOGGER.error("Failed to find all reviews");
            throw new ServiceException(e);
        }
        return reviews;
    }

    public void createReview(Review review) throws ServiceException {
        try {
            REVIEW_DAO.create(review);
        } catch (DaoException e) {
            LOGGER.error("Failed to create review");
            throw new ServiceException(e);
        }
    }

}
