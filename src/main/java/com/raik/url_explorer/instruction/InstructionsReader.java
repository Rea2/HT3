package com.raik.url_explorer.instruction;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Класс предназначен для чтения строк с инструкциями из потока данных(текстового файла), создания из этих строк
 * объектов типа Instruction (рабочих или  служебных), добавления  объектов  Instruction в очередь ArrayDeque,
 * обеспечение доступа к коллекции инстукций через метод nextInstruction(). Перед чтением каждого файла
 * добавляется служебная инструкция типа BEGIN. По окончанию чтения интрукция типа END. Это позволяет
 * прочитать несколько файлов а затем организовать раздельный вывод результатов.
 *
 * @author Raik Yauheni
 */
public class InstructionsReader {
    private Deque<Instruction> instructions = new ArrayDeque<>();
    private TypesInstructions[] typesWorkInstructions;

    public InstructionsReader() {

        // Set subArray with types of working instructions
        typesWorkInstructions = Arrays.copyOfRange(TypesInstructions.values(),
                0, TypesInstructions.ERROR_READING.ordinal());
    }

    public Deque<Instruction> getInstructions() {
        return instructions;
    }

    public boolean addInstructionsFromFile(String filePath) {
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

    /**
     * Создает и возвращает обеъект {@link Instruction}. Метода пытаеся создать рабочую инструкцию из списка
     * поддерживаемых {@link TypesInstructions}.  Если инструкция не может быть распознана,
     *  возвращается инструкция типа ERROR_READING, через метод createErrorInstruction(String line)
     * @param line строка содержащая текст инструкции с параметрами
     * @return объект Instruction
     */
    public Instruction createInstruction(String line) {

        // Check line.  If the line contains supported working instruction, the method will create working instruction
        line = line.trim();
        for (TypesInstructions type : typesWorkInstructions) {
            if (line.startsWith(type.getAbbreviation())) {
                return createWorkingInstruction(type, line);
            }
        }
        // ...otherwise it will create ERROR_READING instruction
        return createErrorInstruction(line);
    }

    /**
     * Созадет служебную интструкцию типа BEGIN.
     * @param file файл из которого выполняется чтение интсрукций
     * @return
     */
    private Instruction createBeginInstruction(File file) {
        return new Instruction(TypesInstructions.BEGIN, file.getAbsolutePath());
    }

    private Instruction createWorkingInstruction(TypesInstructions type, String line) {
        String bodyInstruction = line.replaceFirst(type.getAbbreviation() + " +\"","" );
                bodyInstruction.trim();
                String str = line.replaceFirst("[\"]$", "");
                if (str.length() + 1 != line.length()) {
                    return createErrorInstruction(line);
                }
        bodyInstruction = bodyInstruction.substring(0, bodyInstruction.length()-1);
        String[] arguments  = bodyInstruction.split("\" +\"");
        if (isNumberOfParametersCorrect(type, arguments)) {
            return new Instruction(type, arguments);
        } else {
            return createErrorInstruction(line);
        }
    }

    /**
     * Созадет служебную интструкцию типа ERROR_READING.
     * @param line  строка инструкции, из которой невозможно создать рабочую инструкцию
     * @return
     */
    private Instruction createErrorInstruction(String line) {
        return new Instruction(TypesInstructions.ERROR_READING, line);
    }

    /**
     * Созадет служебную интструкцию типа END.
     * @param file файл из которого выполняется чтение интсрукций
     * @return
     */
    private Instruction createEndOfFileInstruction(File file)  {
        return new Instruction(TypesInstructions.END, file.getAbsolutePath());
    }

    private boolean isNumberOfParametersCorrect(TypesInstructions type, String[] args) {
        if ((type == TypesInstructions.OPEN) && (args.length ==2)) {
            return true;
        }  else if (args.length == 1) {
            return true;
        }
        else return false;
    }
}
