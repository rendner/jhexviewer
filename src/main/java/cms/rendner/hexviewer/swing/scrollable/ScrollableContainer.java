package cms.rendner.hexviewer.swing.scrollable;

import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.swing.separator.JSeparatedView;

import javax.swing.*;

/**
 * Abstract base class which used by the JHexViewer to scroll the content.
 *
 * @author rendner
 * @see JHexViewer#getScrollableByteRowsContainer()
 */
public abstract class ScrollableContainer extends JSeparatedView implements Scrollable
{
}
