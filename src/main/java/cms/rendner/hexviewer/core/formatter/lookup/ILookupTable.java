package cms.rendner.hexviewer.core.formatter.lookup;

import org.jetbrains.annotations.NotNull;

/**
 * Contains precalculated strings mapped by an index.
 *
 * @author rendner
 */
public interface ILookupTable
{
    /**
     * Returns the size of the precalculated char table.
     *
     * @return size of the table &gt;=0.
     */
    int size();

    /**
     * Returns the precalculated string for the value.
     *
     * @param value the value which specifies which char should be returned.
     * @return the char for the value.
     */
    @NotNull
    String mappedValue(int value);
}
