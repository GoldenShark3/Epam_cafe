create table order_status
(
    id          int auto_increment
        primary key,
    status_name varchar(10) not null,
    constraint order_status_status_name_uindex
        unique (status_name)
);

create table payment_method
(
    id          int auto_increment
        primary key,
    method_name varchar(10) not null,
    constraint payment_method_method_name_uindex
        unique (method_name)
);

create table product_type
(
    id           int auto_increment
        primary key,
    type_name    varchar(50)  not null,
    img_filename varchar(255) not null,
    constraint product_type_type_name_uindex
        unique (type_name)
);

create table product
(
    id                  int auto_increment
        primary key,
    product_name        varchar(25)   not null,
    price               decimal(5, 2) not null,
    img_name            varchar(255)  not null,
    product_description varchar(255)  not null,
    type_id             int           not null,
    constraint product_product_name_uindex
        unique (product_name),
    constraint product_type_fk
        foreign key (type_id) references product_type (id)
            on update cascade on delete cascade
);

create table user_role
(
    id        int auto_increment
        primary key,
    role_name varchar(10) not null,
    constraint user_role_role_uindex
        unique (role_name)
);

create table cafe_user
(
    id             int auto_increment
        primary key,
    username       varchar(100)  not null,
    pass           varchar(255)  not null,
    first_name     varchar(45)   not null,
    last_name      varchar(90)   not null,
    email          varchar(255)  not null,
    balance        decimal(8, 2) not null,
    loyalty_points int           not null,
    is_blocked     tinyint(1)    not null,
    phone_number   varchar(18)   not null,
    role_id        int           not null,
    constraint cafe_user_email_uindex
        unique (email),
    constraint cafe_user_username_uindex
        unique (username),
    constraint cafe_user_fk
        foreign key (role_id) references user_role (id)
            on update cascade
);

create table cafe_order
(
    id                int auto_increment
        primary key,
    delivery_address  varchar(50)   not null,
    order_cost        decimal(6, 2) not null,
    create_date       timestamp     not null,
    delivery_date     timestamp     not null,
    payment_method_id int           not null,
    status_id         int           not null,
    user_id           int           not null,
    constraint cafe_order_orderStatus_fk
        foreign key (status_id) references order_status (id)
            on update cascade,
    constraint cafe_order_payment_method_fk
        foreign key (payment_method_id) references payment_method (id)
            on update cascade,
    constraint cafe_order_user_fk
        foreign key (user_id) references cafe_user (id)
            on update cascade on delete cascade
);

create table order_product
(
    order_id   int not null,
    product_id int not null,
    amount     int not null,
    primary key (order_id, product_id),
    constraint order_product_fk
        foreign key (order_id) references cafe_order (id)
            on update cascade,
    constraint product_order_fk
        foreign key (product_id) references product (id)
            on update cascade
);

create table review
(
    id       int auto_increment
        primary key,
    feedback varchar(2048) null,
    rate     int           not null,
    user_id  int           not null,
    constraint review_cafe_user_id_fk
        foreign key (user_id) references cafe_user (id)
);

create index review_user_id_index
    on review (user_id);


