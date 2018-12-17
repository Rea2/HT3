import com.raik.url_explorer.Runner;

/**
 * Данный класс не относится к решению задачи. Использвуется как смоук - тест. Пример частного решения:)
 * @author Raik Yauheni
 */
public class SimpleSmokeTest {

    public static void main(String[] args) throws Exception {

        String[] arguments = { "d", "E:\\Coding\\EPAM Training\\HT3\\src\\test\\java\\instructions1.txt",
                "E:\\Coding\\EPAM Training\\HT3\\target\\logs\\log1.txt",
                "E:\\Coding\\EPAM Training\\HT3\\src\\test\\java\\instructions2.txt" ,
                "E:\\Coding\\EPAM Training\\HT3\\target\\logs\\log2.txt"};

        Runner.main(arguments);
//        new Runner().perform(arguments);
    }
}
