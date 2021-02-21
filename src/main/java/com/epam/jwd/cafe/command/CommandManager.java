package com.epam.jwd.cafe.command;

import com.epam.jwd.cafe.command.impl.ToLoginCommand;
import com.epam.jwd.cafe.command.impl.ToRegistrationCommand;

public enum CommandManager {
    TO_LOGIN(new ToLoginCommand(), "to_login"),
    TO_REGISTRATION(new ToRegistrationCommand(), "to_registration"),
    DEFAULT(request -> null, "commandName");

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
        for (CommandManager action : values()) {
            if (action.name().equalsIgnoreCase(name)) {
                return action.command;
            }
        }
        return DEFAULT.command;
    }

}
