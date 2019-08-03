package cms.rendner.hexviewer.swing.scrollable;

import cms.rendner.hexviewer.core.JHexViewer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Allows customization of the scroll behaviour of a ScrollableRowsContainer.
 *
 * @author rendner
 */
public interface IScrollableDelegate extends Scrollable
{
    /**
     * Installs the delegate to the JHexViewer.
     * <p/>
     * This can be used to gain access to properties of the JHexViewer component.
     * This method is automatically called by the JHexViewer whenever a scroll delegate gets installed.
     *
     * @param hexViewer the JHexViewer component to install to.
     */
    void install(@NotNull JHexViewer hexViewer);

    /**
     * Uninstalls the delegate from the JHexViewer.
     * <p/>
     * This can be used to unregister any listeners that were attached.
     * This method is automatically called by the JHexViewer whenever a scroll delegate gets uninstalled.
     *
     * @param hexViewer the JHexViewer component to uninstall from.
     */
    void uninstall(@NotNull JHexViewer hexViewer);
}
