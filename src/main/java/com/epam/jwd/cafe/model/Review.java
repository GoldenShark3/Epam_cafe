package com.epam.jwd.cafe.model;

import java.util.Objects;

public class Review extends BaseEntity {
    private final Integer id;
    private final String feedback;
    private final Integer rate;
    private final User user;

    private Review(Integer id, String feedback, Integer rate, User user) {
        this.id = id;
        this.feedback = feedback;
        this.rate = rate;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", feedback='" + feedback + '\'' +
                ", rate=" + rate +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(feedback, review.feedback) && Objects.equals(rate, review.rate) && Objects.equals(user, review.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, feedback, rate, user);
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

    public User getUser() {
        return user;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private Integer id;
        private String feedback;
        private Integer rate;
        private User user;

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

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Review build() {
            return new Review(this.id,
                    this.feedback,
                    this.rate,
                    this.user);
        }
    }
}
