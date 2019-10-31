package cms.rendner.hexviewer.common.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Utility to check for required values.
 *
 * @author rendner
 */
public final class CheckUtils
{
    /**
     * Checks if a value is not smaller than required.
     *
     * @param value    value to check.
     * @param minValue minimal expected value.
     * @throws IllegalArgumentException if value is smaller than expected.
     */
    public static void checkMinValue(final int value, final int minValue)
    {
        checkMinValue(value, minValue, "Invalid value '" + value + "', value should at least '" + minValue + "'.");
    }

    /**
     * Checks if a value is not smaller than required.
     *
     * @param value        value to check.
     * @param minValue     minimal expected value.
     * @param errorMessage message to use in case value is smaller than expected.
     * @throws IllegalArgumentException if value is smaller than expected.
     */
    public static void checkMinValue(final int value, final int minValue, @NotNull final String errorMessage)
    {
        if (value < minValue)
        {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks if a value is not smaller than required.
     *
     * @param value    value to check.
     * @param minValue minimal expected value.
     * @throws IllegalArgumentException if value is smaller than expected.
     */
    public static void checkMinValue(final double value, final double minValue)
    {
        if (value < minValue)
        {
            throw new IllegalArgumentException("Invalid value '" + value + "', value should at least '" + minValue + "'.");
        }
    }

    /**
     * Checks if a value is not greater than required.
     *
     * @param value    value to check.
     * @param maxValue maximal expected value.
     * @throws IllegalArgumentException if value is greater than expected.
     */
    public static void checkMaxValue(final int value, final int maxValue)
    {
        checkMaxValue(value, maxValue, "Invalid value '" + value + "', value shouldn't be greater than '" + maxValue + "'.");
    }

    /**
     * Checks if a value is not greater than required.
     *
     * @param value        value to check.
     * @param maxValue     maximal expected value.
     * @param errorMessage the message to use in case value is greater than expected.
     * @throws IllegalArgumentException if value is greater than expected.
     */
    public static void checkMaxValue(final int value, final int maxValue, @NotNull final String errorMessage)
    {
        if (value > maxValue)
        {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Hide constructor.
     */
    private CheckUtils()
    {
    }
}
