package com.raik.url_explorer;

import com.raik.url_explorer.exceptions.CannotCreateLogFileException;
import com.raik.url_explorer.checkers.CheckerArgs;
import com.raik.url_explorer.exceptions.CannotWriteLogException;
import com.raik.url_explorer.exceptions.WrongArgumentsException;
import com.raik.url_explorer.exceptions.WrongInstructionException;
import com.raik.url_explorer.handlers.Handler;
import com.raik.url_explorer.instruction.Instruction;
import com.raik.url_explorer.instruction.InstructionsReader;
import com.raik.url_explorer.loggers.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс обеспечивает последовательный запуск всех модулей фреймворка и управление их работой. Предусмотрено два способа
 * запуска приложения. Оба с передачей параметров (см. класс {@link CheckerArgs} ).
 * Первый способ  - запуск человеком или скриптом из командной строки. В этом случае
 * обработка исключений предусмотрена внутри программы с выводом сообщения об ошибке пользователю. Также в консоль
 * выводятся логи работы.
 * Второй вариант - запуск фреймворка из другого приложения. В этом случае  необходимо создать экземпляр данного класса
 * и запустить метод perform(Sting args[]). В случае возникновения ошибок программа будет генерировать
 * собственные исключения и передавать их вызвавшему приложению.
 * Программа реализует один режим работы (первый параметр d), подробнее см. класс{@link CheckerArgs}.
 *
 * Работа приложения организована следующим образом. Запускается объект класса Runner одним из способов, указанных выше.
 * Далее он передает аргументы на проверку объекту класса {@link CheckerArgs}. Если результат проверки отрицательный,
 * работа приложения прекращается. Если аргументы корректные,  Runner передает пути файлов с инструкциями объекту
 * класса {@linkInstructorReader}. InstructorReader читает данные из файлов и создает очередь объектов класса Instruction.
 * Затем Runner “вытягивает” по одной инструкции из очереди у InstructorReader и передает их обработчику {@link Handler}.
 * Handler выполняет обработку инструкции и возвращает строку с результатом выполнения.
 * Runner накапливает эти инструкции в коллекции и по окончании выполнения  всех инструкций данного файла передает их
 * объекту класса Logger, который осуществляет логирование в файл, указанный в следующем аргументе.

 *
 * @author Raik Yauheni
 */
public class Runner {
    private InstructionsReader instructionsReader;
    private Logger logger;
    private Handler handler;
    private List<String> logMessages = new ArrayList<>();
    private boolean isArgsValidated = false;
    public final static String TAB = "   ";
    public final static String ERROR_USER_MESSAGE = "Dear user. During running error has occurred";
    public final static String FRAME_FOR_ERROR_MESSAGE =
            "--------------------------------------------------------------------------------------------------";
    public final String ILLEGAL_ARGUMENTS = "Please input an even number of arguments: \n" +
            FRAME_FOR_ERROR_MESSAGE + "\n" +
            "The first argument of pair - path to file with instructions.  It has to has \"TXT\" extension." +
            "The second argument of pair - path to log file. If the log file doesn't exist, it will be created. \n" +
            "If it already exists - it will be rewrite\n" +
            FRAME_FOR_ERROR_MESSAGE + "\n";


    public static void main(String[] args) {
        try {
            new Runner().perform(args);
        } catch (CannotCreateLogFileException | CannotWriteLogException | WrongArgumentsException | WrongInstructionException e1) {
            System.out.println(TAB + ERROR_USER_MESSAGE);
            System.out.println(e1.getMessage());
        }
    }

    public void perform(String[] args) throws WrongArgumentsException,
            WrongInstructionException, CannotCreateLogFileException, CannotWriteLogException {
        CheckerArgs checker = new CheckerArgs();
        if (checker.checkArgs(args)) {
            instructionsReader = new InstructionsReader();
            handler = new Handler();
            if (checker.getMode() == "d") {
                performMode_d(args);
            }
        } else {
            throw new WrongArgumentsException(checker.getError_message());
        }
    }

    public void performMode_d(String[] args) throws CannotCreateLogFileException, WrongInstructionException,
            CannotWriteLogException {
        int i = 1;
        while (i < args.length) {

            // Read instruction file, create deque of instructions
            if (instructionsReader.addInstructionsFromFile(args[i])) {
                Instruction inst = null;

                logger = new Logger(args[i + 1]);

                //Poll all the instruction out of deque and pass them to handler one by one
                while (instructionsReader.hasNextInstruction()) {
                    inst = instructionsReader.nextInstruction();
                    String message = handler.execute(inst);

                    // Add the message to collection with result of execution of every either work or error instruction
                    logMessages.add(message);
                }

                //Write messages into file
                try {
                    logger.logging(logMessages);
                } catch (IOException e) {
                    throw new CannotWriteLogException("Can not to write data into log file. " +
                            "Perhaps, either threare are not permissions for this operation " +
                            "or file is being used  by another application", e);
                }
                i += 2;
            }
        }
    }
}

