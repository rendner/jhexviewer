package example;

import cms.rendner.hexviewer.model.data.DefaultDataModel;
import cms.rendner.hexviewer.model.data.IDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Generates a randomized data model used by the example.
 *
 * @author rendner
 */
public class DataModelFactory
{
    /**
     * Creates a randomized data model.
     *
     * @param bytesPerRow the number of bytes displayed per row.
     * @return the data model to use in the example.
     */
    @NotNull
    public static IDataModel createRandomDataModel(final int bytesPerRow)
    {
        final String[] greetings = {
                "Hi.",
                "Hey.",
                "What's up?",
                "Welcome back.",
                "Hello.",
                "Let's go.",
                "Long time no see.",
                "It's been a while since I last saw you.",
                "Nice to see you.",
                "Nice to see you again.",
                "Where have you been hiding?",
                "It's been ages since I've seen you."};

        final String hint = "To start, drag and drop a file!";
        final String emptyRow = new String(new char[bytesPerRow]);

        final String msg = greetings[new Random().nextInt(greetings.length)] + " " + hint;
        final String goodBye = "have fun, rendner";

        return new DefaultDataModel(emptyRow
                + msg
                + new String(new char[countMissingDots(msg, bytesPerRow)])
                + emptyRow
                + goodBye
        );
    }

    private static int countMissingDots(final String msg, final int bytesPerRow)
    {
        return bytesPerRow - (msg.length() % bytesPerRow);
    }
}
