import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iliakan on 21/11/2016.
 */
public class PrintableTaskPdf {

    Address from;
    Address to;
    PdfContentByte canvas;

    int marginLeft = 20;

    public PrintableTaskPdf(Address from, Address to, PdfContentByte canvas) {
        this.from = from;
        this.to = to;
        this.canvas = canvas;
    }

    public void print() throws DocumentException, IOException {

        Font times16 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H,  16, Font.NORMAL);
        Font times14 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H,  14, Font.NORMAL);

        // конверт открытой стороной вниз, полоской влево
        int y = 50;
        int x = marginLeft;

        Phrase phrase = new Phrase("От: " + from.getWhoLines().get(0), times14);

        Rectangle rect = new Rectangle(10, 100, 10, 100);

        ColumnText ct = new ColumnText(canvas);
        ct.setSimpleColumn(rect);
        ct.addElement(new Paragraph("This is the text added in the rectangle"));
        ct.go();

        /*
        drawString("От: " + from.getWhoLines().get(0), x, y);

        for (String line : from.getAddressLines()) {
            y += pg.getFontMetrics().getHeight();
            drawString(line, x, y);
        }

        pg.setFont(times16);
        y += pg.getFontMetrics().getHeight();
        drawString(from.getIndex(), x + 100, y);

        List<String> toAddressLines = new ArrayList<String>();

        for (int i = 0; i< to.getAddressLines().size(); i++) {
            String line = to.getAddressLines().get(i);
            if (i == 0) line =  "Куда: " + line;
            List<String> lineSplit = splitString(line, pg, 300f);
            toAddressLines.addAll(lineSplit);
        }


        List<String> toWhoLines = new ArrayList<String>();

        for (int i = 0; i< to.getWhoLines().size(); i++) {
            String line = to.getWhoLines().get(i);
            if (i == 0) line =  "Кому: " + line;
            List<String> lineSplit = splitString(line, pg, 300f);
            toWhoLines.addAll(lineSplit);
        }

//        System.out.println(lines);

        pg.setFont(times14);

        y = 300;
        x = 280 + marginLeft;

        for (String line : toWhoLines) {
            y += pg.getFontMetrics().getHeight();
            drawString(line, x, y);
        }

        y += 6;

        for (String line : toAddressLines) {
            y += pg.getFontMetrics().getHeight();
            drawString(line, x, y);
        }

        pg.setFont(times16);
        y += pg.getFontMetrics().getHeight();
        drawString(to.getIndex(), x + 50, y);
*/
/*
        for(int tx=-100; tx<1000; tx+=50) {

            for(int ty=-100; ty<1000; ty+=50) {
                drawString(tx+","+ty, tx, ty);
            }
        }

 */

    }
}
