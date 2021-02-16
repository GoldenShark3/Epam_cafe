package com.epam.jwd.cafe.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final Integer id;
    private final String name;
    private final BigDecimal price;
    private final String description;
    private final String imgFileName;

    private Product(int id, String name, BigDecimal price, String description, String imgFileName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgFileName = imgFileName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imgFileName='" + imgFileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(description, product.description) && Objects.equals(imgFileName, product.imgFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, imgFileName);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private Integer id;
        private String name;
        private BigDecimal price;
        private String description;
        private String imgFileName;

        private Builder() {
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withImgFileName(String imgFileName) {
            this.imgFileName = imgFileName;
            return this;
        }

        public Product build() {
            return new Product(this.id,
                    this.name,
                    this.price,
                    this.description,
                    this.imgFileName);
        }
    }
}
