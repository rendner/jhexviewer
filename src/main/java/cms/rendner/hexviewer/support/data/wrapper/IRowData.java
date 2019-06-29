package cms.rendner.hexviewer.support.data.wrapper;

/**
 * Refers to data of a single row of the data model used in the {@link cms.rendner.hexviewer.core.JHexViewer}..
 *
 * @author rendner
 */
public interface IRowData extends IDataPart
{
    /**
     * The index of the row to which this data belongs.
     */
    int rowIndex();

    /**
     * @return number of excluded leading bytes.
     */
    int excludedLeadingBytes();

    /**
     * @return number of excluded trailing bytes.
     */
    int excludedTrailingBytes();
}
