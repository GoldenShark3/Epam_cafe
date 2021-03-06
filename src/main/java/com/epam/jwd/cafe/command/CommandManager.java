package com.epam.jwd.cafe.command;

import com.epam.jwd.cafe.command.impl.AddBalanceCommand;
import com.epam.jwd.cafe.command.impl.AddProductCommand;
import com.epam.jwd.cafe.command.impl.AddProductTypeCommand;
import com.epam.jwd.cafe.command.impl.AddProductToCartCommand;
import com.epam.jwd.cafe.command.impl.AddReviewCommand;
import com.epam.jwd.cafe.command.impl.ChangeLocaleCommand;
import com.epam.jwd.cafe.command.impl.CreateOrderCommand;
import com.epam.jwd.cafe.command.impl.DeleteProductCommand;
import com.epam.jwd.cafe.command.impl.DeleteProductFromCartCommand;
import com.epam.jwd.cafe.command.impl.DeleteProductTypeCommand;
import com.epam.jwd.cafe.command.impl.EditProductCommand;
import com.epam.jwd.cafe.command.impl.EditProductTypeCommand;
import com.epam.jwd.cafe.command.impl.EditProfileCommand;
import com.epam.jwd.cafe.command.impl.LoginCommand;
import com.epam.jwd.cafe.command.impl.LogoutCommand;
import com.epam.jwd.cafe.command.impl.RegistrationCommand;
import com.epam.jwd.cafe.command.impl.ToAccessBlockedCommand;
import com.epam.jwd.cafe.command.impl.ToAddProductCommand;
import com.epam.jwd.cafe.command.impl.ToAddTypeCommand;
import com.epam.jwd.cafe.command.impl.ToCartCommand;
import com.epam.jwd.cafe.command.impl.ToCreateOrderCommand;
import com.epam.jwd.cafe.command.impl.ToErrorPageCommand;
import com.epam.jwd.cafe.command.impl.ToLoginCommand;
import com.epam.jwd.cafe.command.impl.ToMainPageCommand;
import com.epam.jwd.cafe.command.impl.ToMenuCommand;
import com.epam.jwd.cafe.command.impl.ToMenuItemCommand;
import com.epam.jwd.cafe.command.impl.ToMyOrdersCommand;
import com.epam.jwd.cafe.command.impl.ToNotFoundPageCommand;
import com.epam.jwd.cafe.command.impl.ToOrdersCommand;
import com.epam.jwd.cafe.command.impl.ToProfileCommand;
import com.epam.jwd.cafe.command.impl.ToRegistrationCommand;
import com.epam.jwd.cafe.command.impl.ToReviewCommand;
import com.epam.jwd.cafe.command.impl.ToUsersCommand;
import com.epam.jwd.cafe.command.impl.UpdateOrderCommand;
import com.epam.jwd.cafe.command.impl.UpdateUserCommand;

/**
 * The class represent all {@link Command}'s
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public enum CommandManager {
    TO_LOGIN(new ToLoginCommand(), "to_login"),
    TO_REGISTRATION(new ToRegistrationCommand(), "to_registration"),
    TO_MAIN(new ToMainPageCommand(), "to_main"),
    TO_PROFILE(new ToProfileCommand(), "to_profile"),
    TO_USERS(new ToUsersCommand(), "to_users"),
    TO_CART(new ToCartCommand(), "to_cart"),
    TO_ORDERS(new ToOrdersCommand(), "to_orders"),
    TO_MENU(new ToMenuCommand(), "to_menu"),
    TO_ADD_PRODUCT_TYPE(new ToAddTypeCommand(), "to_add_product_type"),
    TO_MENU_ITEM(new ToMenuItemCommand(), "to_menu_item"),
    TO_ADD_PRODUCT(new ToAddProductCommand(), "to_add_product"),
    TO_CREATE_ORDER(new ToCreateOrderCommand(), "to_create_order"),
    TO_REVIEW(new ToReviewCommand(), "to_review"),
    TO_ACCESS_BLOCKED(new ToAccessBlockedCommand(), "to_access_blocked"),
    TO_MY_ORDERS(new ToMyOrdersCommand(), "to_my_orders"),
    LOGIN(new LoginCommand(), "login"),
    LOGOUT(new LogoutCommand(), "logout"),
    REGISTRATION(new RegistrationCommand(), "registration"),
    LOCALE_SWITCH(new ChangeLocaleCommand(), "locale_switch"),
    EDIT_PROFILE(new EditProfileCommand(), "edit_profile"),
    UPDATE_USER(new UpdateUserCommand(), "update_user"),
    UPDATE_ORDER(new UpdateOrderCommand(), "update_order"),
    ADD_PRODUCT_TYPE(new AddProductTypeCommand(), "add_product_type"),
    ADD_PRODUCT(new AddProductCommand(), "add_product"),
    DELETE_PRODUCT_TYPE(new DeleteProductTypeCommand(), "delete_product_type"),
    EDIT_PRODUCT_TYPE(new EditProductTypeCommand(), "edit_product_type"),
    ADD_TO_CART(new AddProductToCartCommand(), "add_to_cart"),
    EDIT_PRODUCT(new EditProductCommand(), "edit_product"),
    DELETE_PRODUCT(new DeleteProductCommand(), "delete_product"),
    DELETE_PRODUCT_FROM_CART(new DeleteProductFromCartCommand(), "delete_product_from_cart"),
    ADD_REVIEW(new AddReviewCommand(), "add_review"),
    CREATE_ORDER(new CreateOrderCommand(), "create_order"),
    ADD_BALANCE_TO_USER(new AddBalanceCommand(), "add_balance_to_user");

    private final Command command;
    private final String commandName;

    CommandManager(Command command, String commandName) {
        this.command = command;
        this.commandName = commandName;
    }

    public Command getCommand() {
        return command;
    }

    public String getCommandName() {
        return commandName;
    }

    static Command of(String name) {
        for (CommandManager action : CommandManager.values()) {
            if (action.getCommandName().equalsIgnoreCase(name)) {
                return action.command;
            }
        }
        return ToNotFoundPageCommand.INSTANCE;
    }
}
