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
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LetterPrinter {


    public void run(Address from, Address to) throws PrintException, IOException {
        String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
        System.out.println("Default printer: " + defaultPrinter);
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        Doc doc = new SimpleDoc(new PrintableTask(from, to), DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);

        DocPrintJob job = service.createPrintJob();
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        pras.add(MediaSizeName.ISO_C5);
        pras.add(OrientationRequested.LANDSCAPE);
        PrintJobWatcher pjw = new PrintJobWatcher(job);
        job.print(doc, pras);
        pjw.waitForDone();
    }



}

