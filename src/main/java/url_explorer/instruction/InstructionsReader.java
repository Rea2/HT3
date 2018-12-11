package url_explorer.instruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class InstructionsReader {
    private Deque<Instruction> instructions = new ArrayDeque<>();
    private TypesInstructions[] typesWorkInstructions;
    private String errorReadInstruction;

    public InstructionsReader() {
        TypesInstructions[] tI = TypesInstructions.values();

        // Getting array types of working instructions
        typesWorkInstructions = Arrays.copyOfRange(tI, 0, tI.length - 2);
    }

    public boolean AddInstructionsFromFile (String path) throws FileNotFoundException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String inputLine;
            while ((inputLine = br.readLine()) != null)   {
                instructions.add(createInstruction(inputLine));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            instructions.add(getEndOfFileInstruction());
            if (instructions.size() != 0) return true;
            else return false;
        }
    }

    public boolean clearQueueInstruction(){
        instructions.clear();
        if (instructions.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Instruction popInstruction()  {
        return  instructions.poll();
    }

    public Instruction createInstruction(String line) {

        // Fast check is instruction correct
        if ((!(line.contains("open") && (!line.contains("check"))))) {
            return getErrorInstruction(line);
        }
        // Check line.  If it is correct, the  method will create working instruction
        for (TypesInstructions type : typesWorkInstructions) {
                if (line.startsWith(type.getDescription())) {
                    return createWorkingInstruction(type, line);
                }
        }
        // ...otherwise  creates ERROR_READING instructions
        return getErrorInstruction(line);
    }

    private Instruction createWorkingInstruction(TypesInstructions type, String line) {
        Instruction result;
        String bodyInstruction =
                line.replaceFirst(type.getDescription() + " \"","" );
        bodyInstruction = bodyInstruction.substring(0, bodyInstruction.length()-1);
        String[] arguments  = bodyInstruction.split("\" \"");
        if (isNumberOfParametarsCorrect(type, arguments)) {
            return new Instruction(type, arguments);
        } else {
            return new Instruction(TypesInstructions.ERROR_READING, Instruction.ERROR_NUMBER_ARGUMENTS);
        }
    }

    private boolean isNumberOfParametarsCorrect(TypesInstructions type, String[] args) {
       if ((type == TypesInstructions.OPEN) && (args.length ==2)) {
           return true;
       }  else if (args.length == 1) {
           return true;
       }
       else return false;
    }

    private Instruction getErrorInstruction(String line) {
        return new Instruction(TypesInstructions.ERROR_READING, line);
    }

    private Instruction getEndOfFileInstruction() {
        return new Instruction(TypesInstructions.END, "");
    }

}
