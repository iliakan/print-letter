import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.io.IOUtils;
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
import java.net.URL;
import java.nio.file.Files;

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

    public void run() throws IOException, DocumentException {

        System.out.println("Reading " + inputFile.getPath());
        InputStream input = Files.newInputStream(inputFile.toPath());

        OutputStream output = Files.newOutputStream(outputFile.toPath());

        Rectangle pageSize = PageSize.A5.rotate();
        Document document = new Document(pageSize, 10, 10, 10, 10);
        PdfWriter writer = PdfWriter.getInstance(document, output);

        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        Constructor constructor = new Constructor(Task.class);
        TypeDescription taskDescription = new TypeDescription(Task.class);
        taskDescription.putListPropertyType("toList", Address.class);
        constructor.addTypeDescription(taskDescription);
        Yaml yaml = new Yaml(constructor);

        /*
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

        /*
        URL classPath = FromFileLetterPrinter.class.getClassLoader().getResource("arial.ttf");
        System.out.println(classPath);
        */

        // need file to allow cyrillic
        byte[] bytes = IOUtils.toByteArray(FromFileLetterPrinter.class.getClassLoader().getResourceAsStream(("arial.ttf")));
        BaseFont baseFontRussian = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes, null);

        Font font14 = new Font(baseFontRussian, 14);
        Font font16 = new Font(baseFontRussian, 16);

        for(Address to: task.getToList()) {
            System.out.println("Print to " + to);

            document.newPage();

            document.add(new Paragraph("От: " + task.getFrom().getWhoLines().get(0), font14));
            for (String line : task.getFrom().getAddressLines()) {
                document.add(new Paragraph(line, font14));
            }

            document.add(new Paragraph(task.getFrom().getIndex(), font16));

            // 295.000000 220.000000 595.000000 420.000000
            // (0,0) is in the left-bottom corner
            // https://kb.itextpdf.com/home/it5kb/faq/how-should-i-interpret-the-coordinates-of-a-rectangle-in-pdf
            Rectangle rect = new Rectangle(pageSize.getWidth() - 320, 10, pageSize.getWidth() - 20, 160);
//            rect.setBorder(Rectangle.BOX);
//            rect.setBorderWidth(1);
//            rect.setBackgroundColor(BaseColor.GRAY);
//            rect.setBorderColor(BaseColor.GREEN);
//            canvas.rectangle(rect);

            ColumnText ct = new ColumnText(canvas);
            ct.setSimpleColumn(rect);

            for (int i = 0; i < to.getAddressLines().size(); i++) {
                String line = to.getAddressLines().get(i);
                if (i == 0) line =  "Куда: " + line;
                ct.addElement(new Paragraph(line, font14));
            }

            for (int i = 0; i < to.getWhoLines().size(); i++) {
                String line = to.getWhoLines().get(i);
                if (i == 0) line =  "Кому: " + line;
                ct.addElement(new Paragraph(line, font14));
            }


            ct.addElement(new Paragraph(to.getIndex(), font16));


            ct.go();

        }

        document.close();
    }
}
