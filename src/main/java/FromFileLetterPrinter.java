import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.FileOptionHandler;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.imageio.ImageIO;
import javax.print.PrintException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Use VipRiser printer to test it
 */
public class FromFileLetterPrinter {


    // @see resources/mail.yml-example
    @Option(name = "--input", handler = FileOptionHandler.class, usage = "Input yml-file (./mail.yml default)")
    public File inputFile = new File("./mail.yml");

    @Option(name = "--output", handler = FileOptionHandler.class, usage = "Output PDF file (./print.pdf default)")
    public File outputFile = new File("./print.pdf");

    public static void main(String[] args) throws IOException, PrintException, DocumentException {

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

    public void run() throws PrintException, IOException, DocumentException {

        System.out.println("Reading " + inputFile.getPath());
        InputStream input = new FileInputStream(inputFile);

        OutputStream output = new FileOutputStream(outputFile);

        Rectangle pageSize = PageSize.A5.rotate();
        // PageSize.A5.rotate() ?
        Document document = new Document(pageSize, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        PdfContentByte canvas = writer.getDirectContent();

        document.open();

        // PdfContentByte canvas = writer.getDirectContent();

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

            // TYPE_INT_RGB - цветная
            // TYPE_BYTE_GRAY
            BufferedImage bImg = new BufferedImage((int)pageSize.getWidth(), (int)pageSize.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

            Graphics2D pg = bImg.createGraphics();
            pg.setColor(Color.WHITE);
            pg.fillRect(0, 0, bImg.getWidth(), bImg.getHeight());

            pg.setColor(Color.BLACK);

//            Graphics2D pg = new PdfGraphics2D(template, pageSize.getWidth(), pageSize.getHeight());

            PrintableTask printableTask = new PrintableTask(task.getFrom(), to, pg);

            printableTask.print();

            ImageIO.write(bImg, "png", new File("./output_image.png"));
            pg.dispose();

            Image image = Image.getInstance(bImg, null);
            image.setAbsolutePosition(0,0);
            image.setBackgroundColor(CMYKColor.WHITE);

            writer.getDirectContentUnder().addImage(image);

            document.close();


        }

    }
}
