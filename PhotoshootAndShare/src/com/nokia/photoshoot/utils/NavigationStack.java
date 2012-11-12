package com.nokia.photoshoot.utils;

import com.nokia.photoshoot.views.CameraView;
import com.nokia.photoshoot.views.MainView;
import java.util.Stack;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * A helper class to ease the navigation between views. Stores the previous
 * navigation steps in a Stack, has a reference to the running MIDlet and
 * manages the displaying of the views. Also quits the application when
 * navigating back but the stack is empty.
 */
public class NavigationStack {

  private Stack stack;
  private MIDlet midlet;

  public NavigationStack(MIDlet midlet) {
    this.midlet = midlet;
    stack = new Stack();
  }

  /**
   * Naviate current
   *
   * @param display
   */
  public void current(Displayable currentView) {
    Display display = Display.getDisplay(midlet);
    display.setCurrent(currentView);
  }

  /**
   * Navigate forward to newView
   *
   * @param newView
   */
  public void forward(Displayable newView) {
    Display display = Display.getDisplay(midlet);
    Displayable view = display.getCurrent();
    if (view instanceof Alert && newView instanceof Alert) {
      // Both are Alerts replace the current one with the new one.
      Displayable previousView = stack.isEmpty() ? null : (Displayable) stack.peek();
      display.setCurrent((Alert) newView, previousView);
    } else {
      stack.push(view);
      display.setCurrent(newView);
    }
  }

  /**
   * Navigate back one view. Quit if on the last view.
   */
  public void back() {
    back(1);
  }

  /**
   * Navigate backs up multiple levels. Useful with Alerts.
   *
   * @param levels
   */
  public void back(int levels) {
    Displayable view = null;
    for (int i = 0; i < levels; i++) {
      if (!stack.isEmpty()) {
        view = (Displayable) stack.pop();
      } else {
        view = null;
      }
    }
    if (view != null) {
      // If view is MainView show the category bar.
      if (view instanceof MainView) {
        ((MainView) view).setCategoryBarElementSelected(0);
        ((MainView) view).setCategoryBarVisible();
      }
      // If view is CameraView start camera.
      if (view instanceof CameraView) {
        ((CameraView) view).start();
      }
      Display.getDisplay(midlet).setCurrent(view);
    } else {
      midlet.notifyDestroyed();
    }
  }

}
