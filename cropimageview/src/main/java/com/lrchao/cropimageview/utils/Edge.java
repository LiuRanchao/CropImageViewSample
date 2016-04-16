package com.lrchao.cropimageview.utils;

/**
 * Description:  Enum representing an edge in the crop window.
 *
 * @author liuranchao
 * @date 16/4/16 下午3:59
 */
public enum Edge {

    LEFT,
    TOP,
    RIGHT,
    BOTTOM;

    // Private Constants ///////////////////////////////////////////////////////

    // Minimum distance in pixels that one edge can get to its opposing edge.
    // This is an arbitrary value that simply prevents the crop window from
    // becoming too small.
    public static final int MIN_CROP_LENGTH_PX = 40;

    // Member Variables ////////////////////////////////////////////////////////

    private float mCoordinate;

    // Public Methods //////////////////////////////////////////////////////////

    /**
     * Sets the coordinate of the Edge. The coordinate will represent the
     * x-coordinate for LEFT and RIGHT Edges and the y-coordinate for TOP and
     * BOTTOM edges.
     *
     * @param coordinate the position of the edge
     */
    public void setCoordinate(float coordinate) {
        mCoordinate = coordinate;
    }

    /**
     * Add the given number of pixels to the current coordinate position of this
     * Edge.
     *
     * @param distance the number of pixels to add
     */
/*    public void offset(float distance) {
        mCoordinate += distance;
    }*/

    /**
     * Gets the coordinate of the Edge
     *
     * @return the Edge coordinate (x-coordinate for LEFT and RIGHT Edges and
     * the y-coordinate for TOP and BOTTOM edges)
     */
    public float getCoordinate() {
        return mCoordinate;
    }

    /**
     * Gets the current width of the crop window.
     */
/*    public static float getWidth() {
        return Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate();
    }*/

    /**
     * Gets the current height of the crop window.
     */
/*    public static float getHeight() {
        return Edge.BOTTOM.getCoordinate() - Edge.TOP.getCoordinate();
    }*/

    // Private Methods /////////////////////////////////////////////////////////
}
