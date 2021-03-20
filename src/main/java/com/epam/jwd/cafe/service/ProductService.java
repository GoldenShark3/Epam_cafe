package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.field.ProductField;
import com.epam.jwd.cafe.dao.impl.ProductDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.Product;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.util.IOUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductService {
    public static final ProductService INSTANCE = new ProductService();
    private static final ProductDao PRODUCT_DAO = ProductDao.INSTANCE;

    private ProductService(){
    }

    public List<Product> findAllProducts() throws DaoException {
       return PRODUCT_DAO.findAll();
    }

    public Optional<Product> findProductById(int id) throws ServiceException {
        return findProductByUniqueField(String.valueOf(id), ProductField.ID);
    }

    public Optional<Product> findProductByName(String name) throws ServiceException {
        return findProductByUniqueField(name, ProductField.NAME);
    }

    public List<Product> findProductsByTypeId(int typeId) throws ServiceException {
        try {
            return PRODUCT_DAO.findByField(String.valueOf(typeId), ProductField.PRODUCT_TYPE_ID);
        } catch (DaoException e) {
            //todo: log
            throw new ServiceException(e);
        }
    }

    public Optional<String> createProduct(Product product) throws ServiceException {
        try {
            Optional<Product> productOptional = findProductByName(product.getName());
            if (!productOptional.isPresent()) {
                PRODUCT_DAO.create(product);
                return Optional.empty();
            }
        } catch (DaoException e) {
            //todo: log
            throw new ServiceException(e);
        }
        return Optional.of("serverMessage.productNameAlreadyTaken");
    }

    public Map<Product, Integer> receiveProducts(Map<Integer, Integer> cart) throws ServiceException {
        Map<Product, Integer> productsInCart = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Optional<Product> productOptional = findProductById(entry.getKey());
            productOptional.ifPresent(product -> productsInCart.put(product, entry.getValue()));
        }
        return productsInCart;
    }

    public BigDecimal calcOrderCost(Map<Product, Integer> productsInCart) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : productsInCart.entrySet()) {
            BigDecimal productPrice = entry.getKey().getPrice();
            BigDecimal amountOfProducts = BigDecimal.valueOf(entry.getValue());
            totalCost = totalCost.add(productPrice.multiply(amountOfProducts));
        }
        return totalCost;
    }

    public void updateProduct(Product product) throws ServiceException {
        try {
            PRODUCT_DAO.update(product);
        } catch (DaoException e) {
            //todo: log
            throw new ServiceException(e);
        }
    }

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

    public void deleteProductById(int productId) throws ServiceException {
        try {
            PRODUCT_DAO.deleteById(productId);
        } catch (DaoException e) {
            //todo: log
            throw new ServiceException(e);
        }
    }

    private Optional<Product> findProductByUniqueField(String searchableField, ProductField nameOfField) throws ServiceException {
        List<Product> products;
        try {
            products = PRODUCT_DAO.findByField(searchableField, nameOfField);
        } catch (DaoException e) {
            //todo: log.error("Failed on a user search");
            throw new ServiceException("Failed search user by unique field", e);
        }
        return ((products.size() > 0) ? Optional.of(products.get(0)) : Optional.empty());
    }
}
