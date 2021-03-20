package com.epam.jwd.cafe.model;

import java.math.BigDecimal;
import java.util.Objects;

public class User extends BaseEntity{
    private final int id;
    private final Role role;
    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final BigDecimal balance;
    private final String phoneNumber;
    private final boolean isBlocked;
    private final int loyaltyPoints;

    private User(int id, Role role, String username, String password, String firstName, String lastName, String email,
                 BigDecimal balance, String phoneNumber, boolean isBlocked, int loyaltyPoints) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.balance = balance;
        this.phoneNumber = phoneNumber;
        this.isBlocked = isBlocked;
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isBlocked=" + isBlocked +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isBlocked == user.isBlocked && loyaltyPoints == user.loyaltyPoints && role == user.role && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(balance, user.balance) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, username, password, firstName, lastName, email, balance, phoneNumber, isBlocked, loyaltyPoints);
    }

    public int getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public String getPassword() {
        return password;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private Role role;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private BigDecimal balance;
        private String phoneNumber;
        private boolean isBlocked;
        private int loyaltyPoints;

        private Builder() {
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withIsBlocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder withLoyaltyPoints(int loyaltyPoints) {
            this.loyaltyPoints = loyaltyPoints;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this.id,
                    this.role,
                    this.username,
                    this.password,
                    this.firstName,
                    this.lastName,
                    this.email,
                    this.balance,
                    this.phoneNumber,
                    this.isBlocked,
                    this.loyaltyPoints);
        }
    }

}
