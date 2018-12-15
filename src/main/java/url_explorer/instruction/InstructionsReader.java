package url_explorer.instruction;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class InstructionsReader {
    private Deque<Instruction> instructions = new ArrayDeque<>();
    private TypesInstructions[] typesWorkInstructions;

    public InstructionsReader() {
        // set subarray with types of working instructions
        typesWorkInstructions = Arrays.copyOfRange(TypesInstructions.values(),
                0, TypesInstructions.ERROR_READING.ordinal());
    }

    public boolean addInstructionsToDequeFromFile(String filePath) {
        File file = new File(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            instructions.add(createBeginInstruction(file));
            String inputLine;
            while ((inputLine = br.readLine()) != null)   {
                instructions.add(createInstruction(inputLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            instructions.add(createEndOfFileInstruction(file));
            if (instructions.size() > 2) return true;
            else return false;
        }
    }

    public boolean clearQueueInstructions(){
        instructions.clear();
        if (instructions.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasNextInstruction(){
        if (instructions.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }

    public Instruction nextInstruction()  {
        return  instructions.poll();
    }

    public Instruction createInstruction(String line) {

        // Check line.  If the line contains supported working instruction, the method will create working instruction
        for (TypesInstructions type : typesWorkInstructions) {
            if (line.startsWith(type.getAbbreviation())) {
                return createWorkingInstruction(type, line);
            }
        }
         // ...otherwise it will create ERROR_READING instruction
        return createErrorInstruction(line);
        }

    private Instruction createBeginInstruction(File file) {
        return new Instruction(TypesInstructions.BEGIN, file.getAbsolutePath());
    }

    private Instruction createWorkingInstruction(TypesInstructions type, String line) {
        Instruction result;
        String bodyInstruction =
                line.replaceFirst(type.getAbbreviation() + " \"","" );
        bodyInstruction = bodyInstruction.substring(0, bodyInstruction.length()-1);
        String[] arguments  = bodyInstruction.split("\" \"");
        if (isNumberOfParametarsCorrect(type, arguments)) {
            return new Instruction(type, arguments);
        } else {
            return new Instruction(TypesInstructions.ERROR_READING, Instruction.ERROR_NUMBER_ARGUMENTS);
        }
    }

    private Instruction createErrorInstruction(String line) {
        return new Instruction(TypesInstructions.ERROR_READING, line);
    }

    private Instruction createEndOfFileInstruction(File file) {
        return new Instruction(TypesInstructions.END, file.getAbsolutePath());
    }

    private boolean isNumberOfParametarsCorrect(TypesInstructions type, String[] args) {
       if ((type == TypesInstructions.OPEN) && (args.length ==2)) {
           return true;
       }  else if (args.length == 1) {
           return true;
       }
       else return false;
    }
}
