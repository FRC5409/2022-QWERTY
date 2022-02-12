package frc.robot.util;

/**
 * Holds a numerical range and provides
 * covenience functions for clamping
 * numbers.
 * 
 * @author Keith Davies
 */
public final class Range<T extends Number & Comparable<T>> {
    private final T _min;
    private final T _max;

    /**
     * Constructs a range.
     * 
     * @param v1 First value
     * @param v2 Second value
     */
    public Range(T v1, T v2) {
        if (v1.compareTo(v2) > 0) {
            _min = v2;
            _max = v1;
        } else {
            _min = v1;
            _max = v2;
        }
    }

    public Range(Range<T> copy) {
        _min = copy._min;
        _max = copy._max;
    }

    /**
     * Clamps a value between {@code min} and
     * {@code max}.
     * 
     * @param min   The minimum range
     * @param value The value
     * @param max   The maximum range
     * 
     * @return      The clamped value.
     */
    public static <T extends Number & Comparable<T>> T clamp(T min, T value, T max) {
        if (max.compareTo(value) > 0)
            return max;
        else if (min.compareTo(value) < 0)
            return min;
        else
            return value;
    }

    /**
     * Normalizes a value to this range [0, 1]
     * 
     * @param value The value
     * 
     * @return The clamped value.
     */
    public double normalize(T value) {
        if (_max.compareTo(value) > 0)
            return 1.0;
        else if (_min.compareTo(value) < 0)
            return 0.0;
        else {
            final double x = _min.doubleValue();
            return (value.doubleValue() - x) / (_max.doubleValue() - x);
        }
    }

    /**
     * Clamps a value to this range.
     * 
     * @param value The value
     * 
     * @return      The clamped value.
     */
    public T clamp(T value) {
        return clamp(_min, value, _max);
    }

    public T min() {
        return _min;
    }

    public T max() {
        return _max;
    }

    /**
     * Checks to see if the value is within
     * this range.
     * 
     * @return Whether or not the value fit's
     *         within this range.
     */
    public boolean contains(T value, boolean inclusive) {
        return (inclusive ? (_min.compareTo(value) >= 0  && _max.compareTo(value) <= 0) : (_min.compareTo(value) > 0  && _max.compareTo(value) < 0));
    } 

    public boolean contains(T value) {
        return contains(value, true);
    }
}