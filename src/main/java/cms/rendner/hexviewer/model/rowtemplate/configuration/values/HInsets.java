package cms.rendner.hexviewer.model.rowtemplate.configuration.values;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author rendner
 */
public final class HInsets
{
    /**
     * The left inset of a row.
     */
    @NotNull
    private final IValue left;

    /**
     * The right inset of a row.
     */
    @NotNull
    private final IValue right;

    /**
     * Creates a new instance with all the values from a builder.
     */
    public HInsets(@NotNull final IValue left, @NotNull final IValue right)
    {
        super();

        this.left = left;
        this.right = right;
    }

    public HInsets(@NotNull final IValue all)
    {
        this(all, all);
    }

    public HInsets(final double all)
    {
        this(new FixedValue(all));
    }

    /**
     * Returns the value for the right inset of a row.
     *
     * @return the right value.
     */
    @NotNull
    public IValue right()
    {
        return right;
    }

    /**
     * Returns the value for the left inset of a row.
     *
     * @return the left value.
     */
    @NotNull
    public IValue left()
    {
        return left;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof HInsets))
        {
            return false;
        }
        HInsets rowInsets = (HInsets) o;
        return left.equals(rowInsets.left) &&
                right.equals(rowInsets.right);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(left, right);
    }
}
