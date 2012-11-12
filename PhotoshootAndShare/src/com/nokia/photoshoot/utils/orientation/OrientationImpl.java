package com.nokia.photoshoot.utils.orientation;

class OrientationImpl extends Orientation implements com.nokia.mid.ui.orientation.OrientationListener {

  public OrientationImpl() {
    // Listen for orientation events
    com.nokia.mid.ui.orientation.Orientation.addOrientationListener(this);
  }

  /**
   * Deliver sizeChanged event to listener
   *
   * @param newDisplayOrientation
   */
  public void displayOrientationChanged(int newDisplayOrientation) {
    com.nokia.mid.ui.orientation.Orientation.setAppOrientation(newDisplayOrientation);
  }

}
