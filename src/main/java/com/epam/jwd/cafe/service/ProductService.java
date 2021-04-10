package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.field.ProductField;
import com.epam.jwd.cafe.dao.impl.ProductDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.VisibleForTesting;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The class provides a business logics of {@link Product}.
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ProductService {
    private final Logger LOGGER = LogManager.getLogger(ProductService.class);
    public static final ProductService INSTANCE = new ProductService();
    private static final ProductDao PRODUCT_DAO = ProductDao.INSTANCE;

    private ProductService(){
    }

    /**
     * Find all products in the database.
     *
     * @return {@link List<Product>}
     * @throws DaoException - if the database access error
     */
    public List<Product> findAllProducts() throws DaoException {
       return PRODUCT_DAO.findAll();
    }

    /**
     * Find product in the database by id.
     *
     * @param id id of the product to be found
     * @return {@link Optional<Product>}<br> Empty optional if product does not exist
     * @throws ServiceException if the database access error
     */
    public Optional<Product> findProductById(int id) throws ServiceException {
        return findProductByUniqueField(String.valueOf(id), ProductField.ID);
    }

    public Optional<Product> findProductByName(String name) throws ServiceException {
        return findProductByUniqueField(name, ProductField.NAME);
    }

    /**
     * Find all products by product type id.
     *
     * @param typeId id of {@link ProductType}
     * @return {@link List<Product>}
     * @throws ServiceException if the database access error
     */
    public List<Product> findProductsByTypeId(int typeId) throws ServiceException {
        try {
            return PRODUCT_DAO.findByField(String.valueOf(typeId), ProductField.PRODUCT_TYPE_ID);
        } catch (DaoException e) {
            LOGGER.error("Failed to find all products with type id = " + typeId);
            throw new ServiceException(e);
        }
    }

    /**
     * Create new product
     *
     * @param product {@link Product} object to add to the database
     * @return {@link Optional<String>} - server message, if product name is already exist
     * @throws ServiceException if database access error
     */
    public Optional<String> createProduct(Product product) throws ServiceException {
        try {
            Optional<Product> productOptional = findProductByName(product.getName());
            if (!productOptional.isPresent()) {
                PRODUCT_DAO.create(product);
                return Optional.empty();
            }
        } catch (DaoException e) {
            LOGGER.error("Failed to create product");
            throw new ServiceException(e);
        }
        return Optional.of("serverMessage.productNameAlreadyTaken");
    }

    /**
     * Receive products from user cart.
     *
     * @param cart {@link Map} of amount and id products
     * @return {@link Map} of amount and {@link Product}
     * @throws ServiceException if the database access error
     */
    public Map<Product, Integer> receiveProducts(Map<Integer, Integer> cart) throws ServiceException {
        Map<Product, Integer> productsInCart = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Optional<Product> productOptional = findProductById(entry.getKey());
            productOptional.ifPresent(product -> productsInCart.put(product, entry.getValue()));
        }
        return productsInCart;
    }

    /**
     * Calculate order cost
     *
     * @param productsInCart {@link Map} of products and amount in user cart
     * @return {@link BigDecimal cost}
     */
    public BigDecimal calcOrderCost(Map<Product, Integer> productsInCart) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : productsInCart.entrySet()) {
            BigDecimal productPrice = entry.getKey().getPrice();
            BigDecimal amountOfProducts = BigDecimal.valueOf(entry.getValue());
            totalCost = totalCost.add(productPrice.multiply(amountOfProducts));
        }
        return totalCost;
    }

    /**
     * Edit product in the database.
     *
     * @param productId    the id of product in database
     * @param productName  new product name
     * @param productPrice new product price
     * @param description  new product description
     * @return {@link Optional<String>} - server message, if product name is already exist<br> or product not found
     * @throws ServiceException if the database access error
     */
    public Optional<String> editProduct(int productId, String productName,
                                        BigDecimal productPrice, String description) throws ServiceException {

        Optional<Product> productOptional = findProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (!product.getName().equals(productName)
                    && findProductByName(productName).isPresent()) {
                return Optional.of("serverMessage.productNameAlreadyTaken");
            }
            Product editedProduct = Product.builder()
                    .withId(productId)
                    .withProductType(product.getProductType())
                    .withName(productName)
                    .withPrice(productPrice)
                    .withImgFileName(product.getImgFileName())
                    .withDescription(description)
                    .build();

            updateProduct(editedProduct);
        } else {
            return Optional.of("serverMessage.productNotFound");
        }
        return Optional.empty();
    }

    /**
     * Delete product in database by id.
     *
     * @param productId id of the {@link Product} object to be deleted
     * @throws ServiceException if the database access error
     */
    public void deleteProductById(int productId) throws ServiceException {
        try {
            PRODUCT_DAO.deleteById(productId);
        } catch (DaoException e) {
            LOGGER.error("Failed to delete product with id = " + productId);
            throw new ServiceException(e);
        }
    }

    private Optional<Product> findProductByUniqueField(String searchableField, ProductField nameOfField) throws ServiceException {
        List<Product> products;
        try {
            products = PRODUCT_DAO.findByField(searchableField, nameOfField);
        } catch (DaoException e) {
            LOGGER.error("Failed on a product search by field = " + nameOfField.name());
            throw new ServiceException("Failed search user by unique field", e);
        }
        return ((products.size() > 0) ? Optional.of(products.get(0)) : Optional.empty());
    }

    private void updateProduct(Product product) throws ServiceException {
        try {
            PRODUCT_DAO.update(product);
        } catch (DaoException e) {
            LOGGER.error("Failed to update product");
            throw new ServiceException(e);
        }
    }
}
