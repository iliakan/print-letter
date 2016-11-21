import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;


public class LetterFormatOptionHandler extends OneArgumentOptionHandler<LetterFormat> {

    public LetterFormatOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super LetterFormat> setter) {
        super(parser, option, setter);
    }

    @Override
    protected LetterFormat parse(String argument) throws NumberFormatException, CmdLineException {
        return LetterFormat.valueOf(argument);
    }
}
