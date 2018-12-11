package url_explorer.instruction;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class Instruction {

    private TypesInstructions typeCommand;
    private List<String> arguments;
    public static final  String ERROR_NUMBER_ARGUMENTS = "It was defined wrong number of parameters";


    public Instruction(TypesInstructions typeCommand, String ... arguments) {
        this.typeCommand = typeCommand;
        this.arguments = Arrays.asList(arguments);
    }

    public TypesInstructions getTypeCommand() {
        return typeCommand;
    }

    public void setTypeCommand(TypesInstructions typeCommand) {
        this.typeCommand = typeCommand;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
}