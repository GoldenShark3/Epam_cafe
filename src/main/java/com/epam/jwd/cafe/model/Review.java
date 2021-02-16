package com.epam.jwd.cafe.model;

import java.util.Objects;

public class Review {
    private final Integer id;
    private final String feedback;
    private final Integer rate;
    private final Order order;

    private Review(Integer id, String feedback, Integer rate, Order order) {
        this.id = id;
        this.feedback = feedback;
        this.rate = rate;
        this.order = order;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", feedback='" + feedback + '\'' +
                ", rate=" + rate +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(feedback, review.feedback) && Objects.equals(rate, review.rate) && Objects.equals(order, review.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, feedback, rate, order);
    }

    public Integer getId() {
        return id;
    }

    public String getFeedback() {
        return feedback;
    }

    public Integer getRate() {
        return rate;
    }

    public Order getOrder() {
        return order;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private Integer id;
        private String feedback;
        private Integer rate;
        private Order order;

        private Builder(){
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withFeedback(String feedback) {
            this.feedback = feedback;
            return this;
        }

        public Builder withRate(int rate) {
            this.rate = rate;
            return this;
        }

        public Builder withOrder(Order order) {
            this.order = order;
            return this;
        }

        public Review build() {
            return new Review(this.id,
                    this.feedback,
                    this.rate,
                    this.order);
        }
    }
}
