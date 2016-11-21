import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.PrinterName;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.Console;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iliakan on 20/11/2016.
 */
public class Test {
    public static void main(String args[]) throws Exception {

        // get default printer
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

        String printerName = defaultPrintService.getName();
        Console console = System.console();

        // we store all the tray in a hashmap
        Map<Integer, Media> trayMap = new HashMap<Integer, Media>(10);

        // we chose something compatible with the printable interface
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;

        // we retrieve all the supported attributes of type Media
        // we can receive MediaTray, MediaSizeName, ...
        Object o = defaultPrintService.getSupportedAttributeValues(Media.class, flavor, null);
        if (o != null && o.getClass().isArray()) {
            for (Media media : (Media[]) o) {
                // we collect the MediaTray available
                System.out.println(media.getValue() + " : " + media + " - " + media.getClass().getName());
                trayMap.put(media.getValue(), media);

            }
        }

        System.exit(1);

        // Manual Feed
        MediaTray selectedTray = (MediaTray) trayMap.get(619);
        System.out.println("Selected tray : " + selectedTray.toString());


        // we have to add the MediaTray selected as attribute
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(selectedTray);

        // we create the printer job, it print a specified document with a set of job attributes
        DocPrintJob job = defaultPrintService.createPrintJob();

        System.out.println("Trying to print an empty page on : " + selectedTray.toString());
        // we create a document that implements the printable interface
        Doc doc = new SimpleDoc(new PrintableDemo(), DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);

        // we print using the selected attributes (paper tray)
        job.print(doc, attributes);

    }

    static class PrintableDemo implements Printable {

        @Override
        public int print(Graphics pg, PageFormat pf, int pageNum) {
            // we print an empty page
            if (pageNum >= 1)
                return Printable.NO_SUCH_PAGE;
            pg.drawString("", 10, 10);
            return Printable.PAGE_EXISTS;
        }
    }
}
