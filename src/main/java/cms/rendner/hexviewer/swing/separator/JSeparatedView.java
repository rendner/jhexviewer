package cms.rendner.hexviewer.swing.separator;

import cms.rendner.hexviewer.swing.BorderlessJComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Container which supports SeparatorPlaceholder.
 * SeparatorPlaceholder can be added like all other swing components. To modify an already added separator
 * use {@link JSeparatedView#getSeparatorPlaceholder(int)}.
 * <p/>
 * The separator is painted inside the parent of this component, if the parent is an instance of {@link JSeparatedViewport}.
 * This reasons are documented in the JSeparatedViewport class.
 *
 * @author rendner
 * @see JSeparatedViewport
 */
public class JSeparatedView extends BorderlessJComponent
{
    /**
     * List of placeholders.
     */
    @NotNull
    private final List<VSeparatorPlaceholder> placeholders = new ArrayList<>();

    /**
     * Creates a new instance.
     */
    public JSeparatedView()
    {
        super();

        // Don't use the BoxLayout (BUG?) -> at least on mac, the max height of a children is "16384" if a box layout is used
        //
        //setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    }

    @Override
    public void setBackground(final Color newColor)
    {
        setOpaque(newColor != null);
        super.setBackground(newColor);
    }

    @Override
    protected void addImpl(final Component comp, final Object constraints, final int index)
    {
        super.addImpl(comp, constraints, index);

        if (comp instanceof VSeparatorPlaceholder && !placeholders.contains(comp))
        {
            placeholders.add((VSeparatorPlaceholder) comp);
        }
    }

    @Override
    public void remove(final int index)
    {
        final Component comp = getComponent(index);
        super.remove(index);
        if (comp instanceof VSeparatorPlaceholder)
        {
            placeholders.remove(comp);
        }
    }

    @Override
    public void removeAll()
    {
        super.removeAll();
        placeholders.clear();
    }

    /**
     * @return the number of available separator placeholders.
     */
    public int getSeparatorPlaceholderCount()
    {
        return placeholders.size();
    }

    /**
     * Returns the corresponding separator placeholder for the index.
     *
     * @param index the index of the n-th separator, in the range [0, getSeparatorPlaceholderCount()-1].
     * @return the separator at the index.
     */
    public VSeparatorPlaceholder getSeparatorPlaceholder(final int index)
    {
        return placeholders.get(index);
    }
}
