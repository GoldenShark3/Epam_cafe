package com.epam.jwd.cafe.command;

/**
 * The interface represent Command pattern for {@link RequestContext}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public interface Command {
    ResponseContext execute(RequestContext request);

    static Command of(String name){
        return CommandManager.of(name);
    }
}
