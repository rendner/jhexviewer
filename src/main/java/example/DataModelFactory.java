package example;

import cms.rendner.hexviewer.core.model.data.DefaultDataModel;
import cms.rendner.hexviewer.core.model.data.IDataModel;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Generates the data model used by the example.
 *
 * @author rendner
 */
public class DataModelFactory
{
    /**
     * Creates a randomized data model.
     *
     * @return the data model to use in the example.
     */
    @NotNull
    public static IDataModel createRandomDataModel()
    {
        final String[] greetings = {
                "Hi.",
                "Hey.",
                "What's up?",
                "Welcome back.",
                "Hello.",
                "Let's go.",
                "Long time no see.",
                "It's been a while.",
                "Nice to see you.",
                "Nice to see you again.",
                "Where have you been hiding?",
                "It's been ages since I've seen you."};

        final String hint = "Drag and drop a file to start...";

        final int greetingIndex = new Random().nextInt(greetings.length);
        final String message = greetings[greetingIndex] + " " + hint;
        return new DefaultDataModel(message);
    }
}
