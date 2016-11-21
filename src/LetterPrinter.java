import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import javax.print.attribute.standard.MediaSizeName;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LetterPrinter {


    protected List<String> splitString(String text, PDFont font, int fontSize, float widthLimit) throws IOException {

        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        text = text.trim();

        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                spaceIndex = text.length();
            }
            String subString = text.substring(0, spaceIndex);
            float size = fontSize * font.getStringWidth(subString) / 1000;
            //System.out.printf("'%s' - %f of %f\n", subString, size, widthLimit);
            if (size > widthLimit) {
                if (lastSpace < 0) {
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                //System.out.printf("'%s' is line\n", subString);
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                //System.out.printf("'%s' is line\n", text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }

        return lines;

    }


    public void run(Address from, Address to, LetterFormat format) throws PrintException, IOException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(format == LetterFormat.A4 ? PDRectangle.A4 : PDRectangle.A5);

        document.addPage(page);

        PDFont font = PDType0Font.load(document, LetterPrinter.class.getResourceAsStream("/resources/arial.ttf"));

        PDPageContentStream contents = new PDPageContentStream(document, page);

        PDRectangle pageSize = page.getMediaBox();
        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();

        int fontSize = 12;
        contents.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
        contents.setFont(font, fontSize);

        float leading = 1.5f * fontSize;

        // FROM

        contents.beginText();
        contents.newLineAtOffset(20, pageWidth - 40);

        contents.showText("От: " + from.getWho());
        contents.newLineAtOffset(0, -leading);

        for (String line : from.getAddressLines()) {
            contents.showText(line);
            contents.newLineAtOffset(0, -leading);
        }

        contents.newLineAtOffset(160, 0);
        contents.setFont(font, fontSize + 2);
        contents.showText(from.getIndex());

        contents.endText();

        // TO

        List<String> lines = new ArrayList<String>();

        for (String addressLine : to.getAddressLines()) {
            List<String> addressLineSplit = splitString(addressLine, font, fontSize, 270f);
            lines.addAll(addressLineSplit);
        }

        System.out.println(lines);

        contents.setFont(font, fontSize);
        contents.beginText();

        contents.newLineAtOffset(pageHeight - 310, lines.size() * (leading + 2) + 50);

        contents.showText("Кому: " + to.getWho());
        contents.newLineAtOffset(0, -leading);

        for (String line : lines) {
            contents.showText(line);
            contents.newLineAtOffset(0, -leading);
        }

        contents.newLineAtOffset(160, 4);
        contents.setFont(font, fontSize + 2);
        contents.showText(to.getIndex());

        contents.endText();
        contents.close();

        File docFile = new File("/Users/iliakan/doc.pdf");
        /*
        File docFile = File.createTempFile("doc", ".pdf");
        docFile.deleteOnExit();  */

        document.save(docFile);
        document.close();

        System.exit(1);
        String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
        System.out.println("Default printer: " + defaultPrinter);
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        // InputStream is = new ByteArrayInputStream("hello world!\f".getBytes("UTF8"));

        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        pras.add(format == LetterFormat.A4 ? MediaSizeName.ISO_A4 : MediaSizeName.ISO_A5);
        pras.add(new Copies(1));

        DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;

//        PDStream ps = new PDStream(document);


        InputStream in = new FileInputStream(docFile);

        Doc doc = new SimpleDoc(in, flavor, null);
        DocPrintJob job = service.createPrintJob();

        PrintJobWatcher pjw = new PrintJobWatcher(job);
        job.print(doc, pras);
        pjw.waitForDone();
    }
}

