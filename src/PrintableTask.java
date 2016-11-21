import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by iliakan on 21/11/2016.
 */
public class PrintableTask implements Printable {

    Address from;
    Address to;
    Graphics pg;

    public PrintableTask(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected void drawString(String str, int x, int y) {
        System.out.println(str + ' ' + x + ',' + y);
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
    @Override
    public int print(Graphics pg, PageFormat pf, int pageNum) {
        if (pageNum >= 1) {
            return Printable.NO_SUCH_PAGE;
        }

        this.pg = pg;
        Font font = new Font("TimesRoman", Font.PLAIN, 14);
        pg.setFont(font);


        // конверт открытой стороной вниз, полоской влево
        int y = 50;
        int x = 60;
        drawString("От: " + from.getWho(), x, y);

        for (String line : from.getAddressLines()) {
            y += pg.getFontMetrics().getHeight();
            drawString(line, x, y);
        }

        y += pg.getFontMetrics().getHeight();
        pg.setFont(font.deriveFont(16));
        drawString(from.getIndex(), x + 100, y);


        List<String> lines = new ArrayList<String>();

        for (String addressLine : to.getAddressLines()) {
            List<String> addressLineSplit = splitString(addressLine, pg, 270f);
            lines.addAll(addressLineSplit);
        }

        System.out.println(lines);

        pg.setFont(font.deriveFont(14));

        y = 350;
        x = 360;

        drawString("Кому: " + to.getWho(), x, y);

        for (String line : lines) {
            y += pg.getFontMetrics().getHeight();
            drawString(line, x, y);
        }

        y += pg.getFontMetrics().getHeight();
        pg.setFont(font.deriveFont(14));
        drawString(to.getIndex(), x + 50, y);

        /*
        for(int x=-100; x<1000; x+=50) {

            for(int y=-100; y<1000; y+=50) {
                drawString(x+","+y, x, y);
            }
        }      */

        return Printable.PAGE_EXISTS;
    }
}
