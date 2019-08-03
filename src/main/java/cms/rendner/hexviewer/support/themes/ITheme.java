package cms.rendner.hexviewer.support.themes;

import cms.rendner.hexviewer.core.JHexViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Provides a fast and easy way to overwrite some parts of the JHexViewer without implementing a complete component
 * look-and-feel.
 *
 * @author rendner
 */
public interface ITheme
{
    /**
     * Applies all modifications to the JHexViewer.
     *
     * @param hexViewer reference to the {@link JHexViewer} component.
     */
    void applyTo(@NotNull JHexViewer hexViewer);
}
