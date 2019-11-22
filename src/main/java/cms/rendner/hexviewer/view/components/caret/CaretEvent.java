package cms.rendner.hexviewer.view.components.caret;

import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

/**
 * CaretEvent, used to notify interested parties that the position of the caret has changed.
 *
 * @author rendner
 */
public final class CaretEvent extends EventObject
{
    private final long oldDot;
    private final long newDot;
    private final long oldMark;
    private final long newMark;

    /**
     * Creates a new instance with the specified values.
     *
     * @param caret   the caret which sent the event.
     * @param oldDot  the previous position of the dot.
     * @param oldMark the previous position of the mark.
     * @param newDot  the new position of the dot.
     * @param newMark the new position of the mark.
     */
    public CaretEvent(@NotNull final ICaret caret,
                      final long oldDot,
                      final long oldMark,
                      final long newDot,
                      final long newMark)
    {
        super(caret);
        this.oldDot = oldDot;
        this.oldMark = oldMark;
        this.newDot = newDot;
        this.newMark = newMark;
    }

    /**
     * @return the caret which sent the event
     */
    @NotNull
    public ICaret getCaret()
    {
        return (ICaret) getSource();
    }


    /**
     * @return the previous position of the dot.
     */
    public long getOldDot()
    {
        return oldDot;
    }

    /**
     * @return the new position of the dot.
     */
    public long getNewDot()
    {
        return newDot;
    }

    /**
     * @return the previous position of the mark.
     */
    public long getOldMark()
    {
        return oldMark;
    }

    /**
     * @return the new position of the mark.
     */
    public long getNewMark()
    {
        return newMark;
    }
}
