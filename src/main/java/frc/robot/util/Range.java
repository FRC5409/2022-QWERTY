package frc.robot.util;

/**
 * Holds a numerical range and provides
 * covenience functions for clamping
 * numbers.
 * 
 * @author Keith Davies
 */
public final class Range {
    private final double _min;
    private final double _max;

    /**
     * Constructs a range.
     * 
     * @param v1 First value
     * @param v2 Second value
     */
    public Range(double v1, double v2) {
        if (v1 > v2) {
            _min = v2;
            _max = v1;
        } else {
            _min = v1;
            _max = v2;
        }
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
    public static double clamp(double min, double value, double max) {
        return (value > max) ? max : ((value < min) ? min : value);
    }

    /**
     * Normalizes a value to this range [0, 1]
     * 
     * @param value The value
     * 
     * @return The clamped value.
     */
    public double normalize(double value) {
        return (value > _max) ? 1.0 : ((value < _min) ? 0.0 : (value - _min) / (_max - _min));
    }

    /**
     * Normalizes a value [0, 1] to this range [min, max]
     * 
     * @param value The value
     * 
     * @return The clamped value.
     */
    public double scale(double value) {
        return (value > 1.0) ? _max : ((value < 0.0) ? _min : _min + value * (_max - _min));
    }

    /**
     * Clamps a value to this range.
     * 
     * @param value The value
     * 
     * @return      The clamped value.
     */
    public double clamp(double value) {
        return clamp(_min, value, _max);
    }

    /**
     * Checks to see if the value is within
     * this range.
     * 
     * @return Whether or not the value fit's
     *         within this range.
     */
    public boolean contains(double value, boolean inclusive) {
        return (inclusive ? (value >= _min  && value <= _max) : (value >= _min  && value <= _max));
    } 

    public boolean contains(double value) {
        return contains(value, true);
    }

    public double min() {
        return _min;
    }

    public double max() {
        return _max;
    }

    public double magnitude() {
        return _max - _min;
    }

    public double mid() {
        return (_max  + _min) * 0.5;
    }
}