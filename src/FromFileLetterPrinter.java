import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.FileOptionHandler;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.print.PrintException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Use VipRiser printer to test it
 */
public class FromFileLetterPrinter {


    // @see resources/mail.yml-example
    @Option(name = "--input", handler = FileOptionHandler.class, usage = "Input yml-file (./mail.yml default)")
    public File inputFile = new File("./mail.yml");

    @Option(name = "--format", handler = LetterFormatOptionHandler.class, usage = "Format A4 or A5 (default)")
    public LetterFormat format = LetterFormat.A5;

    public static void main(String[] args) throws IOException, PrintException {

        FromFileLetterPrinter printer = new FromFileLetterPrinter();
        CmdLineParser parser = new CmdLineParser(printer);
        try {
            parser.parseArgument(args);
            printer.run();
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }

    public void run() throws PrintException, IOException {

        System.out.println("Reading " + inputFile.getPath());
        InputStream input = new FileInputStream(inputFile);

        Constructor constructor = new Constructor(Task.class);
        TypeDescription taskDescription = new TypeDescription(Task.class);
        taskDescription.putListPropertyType("toList", Address.class);
        constructor.addTypeDescription(taskDescription);
        Yaml yaml = new Yaml(constructor);

        /**
         * Example:
         *
         * from:
         *   who: ...
         *   ...
         * toList:
         *   - who: "who"
         *     addressLines:
         *       - "addressLine 1"
         *     index: "123456"
         */


        Task task = (Task) yaml.load(input);

        for(Address to: task.getToList()) {
            System.out.println("Print to " + to);
            LetterPrinter letterPrinter = new LetterPrinter();
            letterPrinter.run(task.getFrom(), to, format);
        }

    }
}
