package cms.rendner.hexviewer.core.uidelegate.damager;

import cms.rendner.hexviewer.core.view.areas.AreaId;

/**
 * @author rendner
 */
public class DefaultDamager extends AbstractDamager
{
    /**
     * Damages the position of the caret in all parts where the caret is displayed.
     * <p/>
     * Also damages the row of the caret in the {@link AreaId#OFFSET} if the position of the caret should be displayed.
     *
     * @param oldCaretIndex the position of the caret before a change.
     * @param newCaretIndex the position of the caret after a change.
     * @see IDamager#damageCaret(int, int)
     */
    @Override
    public void damageCaret(final int oldCaretIndex, final int newCaretIndex)
    {
        super.damageCaret(oldCaretIndex, newCaretIndex);

        if (oldCaretIndex != newCaretIndex)
        {
            if (hexViewer.isShowOffsetCaretIndicator())
            {
                final int oldRowIndex = hexViewer.byteIndexToRowIndex(oldCaretIndex);
                final int newRowIndex = hexViewer.byteIndexToRowIndex(newCaretIndex);

                // the offset-row-view displays the offset of the caret in the row of the caret
                damageRow(AreaId.OFFSET, oldRowIndex);

                if (oldRowIndex != newRowIndex)
                {
                    damageRow(AreaId.OFFSET, newRowIndex);
                }
            }
        }
    }
}
