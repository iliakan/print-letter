import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by iliakan on 21/11/2016.
 */
public class PrintableTask {

    Address from;
    Address to;
    Graphics pg;

    int marginLeft = 20;

    public PrintableTask(Address from, Address to, Graphics pg) {
        this.from = from;
        this.to = to;
        this.pg = pg;
    }

    protected void drawString(String str, int x, int y) {
//        System.out.println(str + ' ' + x + ',' + y);
        pg.drawString(str, x, y);
    }


    protected java.util.List<String> splitString(String text, Graphics g, float widthLimit) {

        java.util.List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        text = text.trim();

        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                spaceIndex = text.length();
            }
            String subString = text.substring(0, spaceIndex);
            float size = g.getFontMetrics().stringWidth(subString);
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
    public void print() throws DocumentException, IOException {

        /*

        FileInputStream fontStream = new FileInputStream("/tmp/arial.ttf");

        Font baseFont;
        try {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch(FontFormatException e) {
            throw new RuntimeException(e);
        }

        Font times14 = baseFont.deriveFont(Font.PLAIN, 14);
        Font times16 = baseFont.deriveFont(Font.PLAIN, 16);

         */

        Font times16 = new Font("Times New Roman", Font.PLAIN, 16);
        Font times14 = new Font("Times New Roman", Font.PLAIN, 14);

        pg.setFont(times14);

        // конверт открытой стороной вниз, полоской влево
        int y = 50;
        int x = marginLeft;
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

/*
        for(int tx=-100; tx<1000; tx+=50) {

            for(int ty=-100; ty<1000; ty+=50) {
                drawString(tx+","+ty, tx, ty);
            }
        }

 */

    }
}
