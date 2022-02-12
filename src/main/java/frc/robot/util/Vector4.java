package frc.robot.util;

/**
 * Simple 4D Cartesian plane
 * point / vector / coordinate.
 * 
 * @author Keith Davies
 */
public class Vector4 {
    /**
     * Construct Blank Vector4.
     */
    public Vector4() {
        x = 0;
        y = 0;
        z = 0;
        w = 0;
    }

    /**
     * Construct Vector4 with coordinates.
     * 
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     * @param w W Coordinate
     */
    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * X Coordinate
     */
    public double x;
    
    /**
     * Y Coordinate
     */
    public double y;
    
    /**
     * Z Coordinate
     */
    public double z;
    
    /**
     * W Coordinate
     */
    public double w;
}

