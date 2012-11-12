package com.nokia.photoshoot.utils.orientation;

/**
 * Orientation is supported for Java Runtime 2.0.0 for Series 40 onwards.
 */
public abstract class Orientation {

  private static Orientation orientation;

  /**
   * Set oritentation listener
   */
  public static void enableOrientations() {
    if (orientation == null) {
      orientation = getImplementation();
    }
  }

  /**
   * Loads up the isolated implementation class
   *
   * @return Orientation Returns Orientation implementation or null, if orientations are not supported
   */
  private static Orientation getImplementation() {
    Orientation implementation = null;
    try {
      Class.forName("com.nokia.mid.ui.orientation.OrientationListener");
      Class c = Class.forName("com.nokia.photoshoot.utils.orientation.OrientationImpl");
      implementation = (Orientation) (c.newInstance());
    } catch (Exception ex) {
      // No orientation support
    }
    return implementation;
  }

}
