package cms.rendner.hexviewer.view.themes;

import cms.rendner.hexviewer.view.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Provides a quick and easy way to change the visual look of {@link JHexViewer} without implementing a complete
 * ui-delegate.
 *
 * @author rendner
 */
public interface ITheme
{
    /**
     * Applies all modifications to a {@link JHexViewer} component.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    void applyTo(@NotNull JHexViewer hexViewer);
}
