package com.epam.jwd.cafe.command;

import com.epam.jwd.cafe.command.impl.RegistrationCommand;
import com.epam.jwd.cafe.command.impl.ToNotFoundPageCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandManagerTest {

    @Test
    public void of_ShouldReturnRegistrationCommand() {
        String command = CommandManager.REGISTRATION.getCommandName();

        assertTrue(CommandManager.of(command) instanceof RegistrationCommand);
    }

    @Test
    public void of_ShouldReturnToPageNotFoundCommand_WhenCommandIsNotFound() {
        String command = "not exist command";

        assertTrue(CommandManager.of(command) instanceof ToNotFoundPageCommand);
    }
}