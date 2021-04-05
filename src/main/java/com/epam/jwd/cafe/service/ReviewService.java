package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.impl.ReviewDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

/**
 * The class provides a business logics of {@link Review}.
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ReviewService {
    private final Logger LOGGER = LogManager.getLogger(ProductTypeService.class);
    public static final ReviewService INSTANCE = new ReviewService();
    private static final ReviewDao REVIEW_DAO = ReviewDao.INSTANCE;

    private ReviewService(){
    }

    /**
     * Find all reviews in database
     *
     * @return {@link List<Review>}
     * @throws ServiceException - if database access error
     */
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

    /**
     * Create a new review.
     *
     * @param review - review object to add to the database
     * @throws ServiceException - if database access error
     */
    public void createReview(Review review) throws ServiceException {
        try {
            REVIEW_DAO.create(review);
        } catch (DaoException e) {
            LOGGER.error("Failed to create review");
            throw new ServiceException(e);
        }
    }

}
