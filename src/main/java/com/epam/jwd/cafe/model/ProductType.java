package com.epam.jwd.cafe.model;

import java.util.Objects;

/**
 * The class representation of product type
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ProductType extends BaseEntity{
    private final Integer id;
    private final String name;
    private final String fileName;

    private ProductType(Integer id, String name, String fileName) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
    }


    @Override
    public String toString() {
        return "ProductType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductType that = (ProductType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fileName);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String filename;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }


        public Builder withFileName(String fileName) {
            this.filename = fileName;
            return this;
        }

        public ProductType build() {
            return new ProductType(this.id, this.name, this.filename);
        }
    }
}
