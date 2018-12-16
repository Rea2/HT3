package com.raik.url_explorer.instruction;

import com.raik.url_explorer.handlers.Handler;

/**
 * Класс представляет собой инструкцию, включающую тип и аргументы. Используется для передачи информации обработчику
 * {@link Handler}
 * @author Raik Yauheni
 */
public class Instruction {

    private TypesInstructions typeCommand;
    private String[] arguments;
    public static final  String ERROR_NUMBER_ARGUMENTS = "It was defined wrong number of parameters";

    public Instruction(TypesInstructions typeCommand, String ... arguments) {
        this.typeCommand = typeCommand;
        this.arguments = arguments;
    }

    public TypesInstructions getType() {
        return typeCommand;
    }

    public void setTypeCommand(TypesInstructions typeCommand) {
        this.typeCommand = typeCommand;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
