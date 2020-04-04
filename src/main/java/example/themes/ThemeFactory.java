package example.themes;

import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.themes.ITheme;
import example.themes.alternating.ZebraTheme;
import example.themes.console.ConsoleTheme;
import example.themes.retro.RetroTheme;
import example.themes.simple.SimpleTheme;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Picks a random theme for the {@link JHexViewer}.
 *
 * @author rendner
 */
public class ThemeFactory
{
    @NotNull
    private static final List<ITheme> availableThemes = new ArrayList<>();

    static
    {
        availableThemes.add(new SimpleTheme());
        availableThemes.add(new ConsoleTheme());
        availableThemes.add(new RetroTheme());
        availableThemes.add(new ZebraTheme());
    }

    public static void applyRandomTheme(@NotNull final JHexViewer hexViewer)
    {
        if (!availableThemes.isEmpty())
        {
            final int themeIndex = new Random().nextInt(availableThemes.size());
            final ITheme theme = availableThemes.get(themeIndex);
            theme.applyTo(hexViewer);
        }
    }
}
