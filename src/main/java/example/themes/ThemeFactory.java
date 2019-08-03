package example.themes;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.support.themes.ITheme;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author rendner
 */
public class ThemeFactory
{
    @NotNull
    private static final List<ITheme> availableThemes = new ArrayList<>();

    static
    {
        availableThemes.add(new DarkTheme());
    }

    public static void applyRandomTheme(@NotNull final JHexViewer hexViewer)
    {
        final int themeIndex = new Random().nextInt(availableThemes.size());
        if (themeIndex < availableThemes.size())
        {
            final ITheme theme = availableThemes.get(themeIndex);
            theme.applyTo(hexViewer);
        }
    }
}
