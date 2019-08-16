package cms.rendner.hexviewer.core.model.row.template;

import cms.rendner.hexviewer.core.model.row.template.elements.IElement;
import cms.rendner.hexviewer.utils.CheckUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Describes the layout of a row of bytes.
 * <p/>
 * Row templates are used to describe the layout of the rows displayed in the JHexViewer.
 * For each of the two byte areas ({@link cms.rendner.hexviewer.core.view.areas.AreaId#HEX} and
 * {@link cms.rendner.hexviewer.core.view.areas.AreaId#ASCII}) of the JHexViewer a separate template
 * exists which describes the exact layout of the rows rendered by these two areas.
 *
 * @author rendner
 */
public final class ByteRowTemplate extends RowTemplate implements IByteRowTemplate
{
    /**
     * The width of the caret which can be placed between the bytes of the row.
     */
    private final int caretWidth;

    /**
     * Hide the constructor.
     * Creates a new instance with all the values from a builder.
     *
     * @param source the builder used to initialize the new instance.
     */
    private ByteRowTemplate(@NotNull final Builder source)
    {
        super(source);
        this.caretWidth = source.caretWidth;
    }

    /**
     * Returns a new builder for this class.
     *
     * @return the created builder.
     */
    @NotNull
    public static Builder newBuilder()
    {
        return new Builder();
    }

    @NotNull
    @Override
    public Rectangle caretBounds(final int byteIndex)
    {
        final IElement byteAfterCaret = elements.get(byteIndex);
        return new Rectangle(
                byteAfterCaret.x() - caretWidth,
                byteAfterCaret.y(),
                caretWidth,
                byteAfterCaret.height()
        );
    }

    /**
     * Builder to configure and create ByteRowTemplate instances.
     */
    public static class Builder extends RowTemplate.Builder<Builder>
    {
        /**
         * The width of the caret which can be placed between the bytes of the row.
         */
        private int caretWidth = 1;

        /**
         * Hide the constructor.
         * Creates a new builder.
         */
        private Builder()
        {
            super();
        }

        @Override
        protected Builder getThis()
        {
            return this;
        }

        /**
         * Sets the width of the caret which can be placed between the bytes of the row.
         *
         * @param caretWidth the width for the caret, &gt;= 1.
         * @return the builder instance, to allow method chaining.
         */
        public Builder setCaretWidth(final int caretWidth)
        {
            CheckUtils.checkMinValue(caretWidth, 1);
            this.caretWidth = caretWidth;
            return this;
        }

        /**
         * @return a new instance with the configured values.
         */
        public ByteRowTemplate build()
        {
            return new ByteRowTemplate(this);
        }
    }
}
