package cms.rendner.hexviewer.core.view.areas;

/**
 * A typesafe enumeration of the areas of the hex viewer.
 *
 * @author rendner
 */
public enum AreaId
{
    /**
     * The id of the area which displays the row offsets.
     */
    OFFSET,

    /**
     * The id of the area which displays bytes in hex format.
     */
    HEX,

    /**
     * The id of the area which displays bytes in ascii format.
     */
    ASCII
}
