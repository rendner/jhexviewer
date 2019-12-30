package cms.rendner.hexviewer.view.components.areas.common;

import cms.rendner.hexviewer.view.JHexViewer;

/**
 * A typesafe enumeration of the areas of the {@link JHexViewer}.
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
     * The id of the area which displays bytes as text.
     */
    TEXT
}
