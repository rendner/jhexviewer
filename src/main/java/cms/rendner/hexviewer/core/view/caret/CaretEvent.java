package cms.rendner.hexviewer.core.view.caret;

import cms.rendner.hexviewer.core.view.geom.IndexPosition;
import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

/**
 * CaretEvent, used to notify interested parties that the position of the caret has changed in the JHexViewer.
 *
 * @author rendner
 */
public class CaretEvent extends EventObject
{
    @NotNull
    private final IndexPosition oldDot;
    @NotNull
    private final IndexPosition newDot;
    @NotNull
    private final IndexPosition oldMark;
    @NotNull
    private final IndexPosition newMark;

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
                      @NotNull final IndexPosition oldDot,
                      @NotNull final IndexPosition oldMark,
                      @NotNull final IndexPosition newDot,
                      @NotNull final IndexPosition newMark)
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
    @NotNull
    public IndexPosition getOldDot()
    {
        return oldDot;
    }

    /**
     * @return the new position of the dot.
     */
    @NotNull
    public IndexPosition getNewDot()
    {
        return newDot;
    }

    /**
     * @return the previous position of the mark.
     */
    @NotNull
    public IndexPosition getOldMark()
    {
        return oldMark;
    }

    /**
     * @return the new position of the mark.
     */
    @NotNull
    public IndexPosition getNewMark()
    {
        return newMark;
    }
}
