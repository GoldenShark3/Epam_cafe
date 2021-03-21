package com.epam.jwd.cafe.service;

import com.epam.jwd.cafe.dao.field.ProductTypeField;
import com.epam.jwd.cafe.dao.impl.ProductTypeDao;
import com.epam.jwd.cafe.exception.DaoException;
import com.epam.jwd.cafe.exception.ServiceException;
import com.epam.jwd.cafe.model.ProductType;
import com.epam.jwd.cafe.util.IOUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ProductTypeService {
    private final Logger LOGGER = LogManager.getLogger(ProductTypeService.class);
    public static final ProductTypeService INSTANCE = new ProductTypeService();
    private final ProductTypeDao PRODUCT_TYPE_DAO = ProductTypeDao.INSTANCE;

    private ProductTypeService() {
    }

    public List<ProductType> findAllProductTypes() throws ServiceException {
        try {
            return PRODUCT_TYPE_DAO.findAll();
        } catch (DaoException e) {
            LOGGER.error("Failed to find all product types");
            throw new ServiceException(e);
        }
    }

    public Optional<ProductType> findProductTypeById(int id) throws ServiceException {
        return findProductTypeByUniqueField(String.valueOf(id), ProductTypeField.ID);
    }

    public Optional<ProductType> findProductTypeByName(String productTypeName) throws ServiceException {
        return findProductTypeByUniqueField(productTypeName, ProductTypeField.NAME);
    }

    public Optional<String> createProductType(ProductType productType) throws ServiceException {
        try {
            if (!findProductTypeByName(productType.getName()).isPresent()) {
                PRODUCT_TYPE_DAO.create(productType);
                return Optional.empty();
            }
        } catch (DaoException e) {
            LOGGER.error("Failed to create product type");
            throw new ServiceException(e);
        }
        return Optional.of("serverMessage.productTypeNameAlreadyTaken");
    }

    public void updateProductType(ProductType productType) throws ServiceException {
        try {
            PRODUCT_TYPE_DAO.update(productType);
        } catch (DaoException e) {
            LOGGER.error("Failed to update product type");
            throw new ServiceException(e);
        }
    }

    public Optional<String> editProductType(int id, String newFileName, String newProductName) throws ServiceException {
        Optional<ProductType> productTypeOptional = findProductTypeById(id);

        if (productTypeOptional.isPresent()) {
            ProductType productType = productTypeOptional.get();
            if (!productType.getName().equals(newProductName)
                    && findProductTypeByName(newProductName).isPresent()) {
                return Optional.of("serverMessage.productTypeNameAlreadyTaken");
            }
            ProductType editedProductType = ProductType.builder()
                    .withId(productType.getId())
                    .withName(newProductName)
                    .withFileName(newFileName)
                    .build();

            updateProductType(editedProductType);
            IOUtil.deleteData(productType.getFileName());
        } else {
            return Optional.of("serverMessage.productTypeNotFound");
        }
        return Optional.empty();
    }

    public void deleteProductTypeById(int productTypeId) throws ServiceException {
        try {
            PRODUCT_TYPE_DAO.deleteById(productTypeId);
        } catch (DaoException e) {
            LOGGER.error("Failed to delete product type with id = " + productTypeId);
            throw new ServiceException(e);
        }
    }

    private Optional<ProductType> findProductTypeByUniqueField(String searchableField, ProductTypeField nameOfField) throws ServiceException {
        List<ProductType> productTypes;
        try {
            productTypes = PRODUCT_TYPE_DAO.findByField(searchableField, nameOfField);
        } catch (DaoException e) {
            LOGGER.error("Failed on a product type search with field = " + nameOfField);
            throw new ServiceException("Failed search product type by unique field", e);
        }
        return ((productTypes.size() > 0) ? Optional.of(productTypes.get(0)) : Optional.empty());
    }

}
