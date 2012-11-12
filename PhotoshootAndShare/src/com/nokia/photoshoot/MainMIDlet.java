package com.nokia.photoshoot;

import com.nokia.photoshoot.views.MainView;
import com.nokia.photoshoot.utils.orientation.Orientation;
import com.nokia.photoshoot.utils.NavigationStack;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

/**
 * Photoshoot & Share main MIDlet.
 *
 * @author Aniwat Keereena
 * @since 21/10/2012
 */
public class MainMIDlet extends MIDlet {

  private Display display;
  private NavigationStack navigation;
  private MainView mainView;

  public MainMIDlet() {
    // Enable orientation support
    Orientation.enableOrientations();

    display = Display.getDisplay(this);
    navigation = new NavigationStack(this);
    mainView = new MainView(this);
  }

  public void startApp() {
    navigation.forward(mainView);
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
  }

  public Display getDisplay() {
    return display;
  }

  /**
   * Navigation current.
   *
   * @param currentView
   */
  public void navigationCurrent(Displayable currentView) {
    navigation.current(currentView);
  }

  /**
   * Navigation back.
   */
  public void navigationBack() {
    navigation.back();
  }

  /**
   * Navigaion forward.
   *
   * @param newView
   */
  public void navigationForward(Displayable newView) {
    navigation.forward(newView);
  }

  /**
   * Show message.
   *
   * @param message
   * @param type
   */
  public void showMessage(String message, AlertType type) {
    Alert info = new Alert(null, message, null, type);
    info.setTimeout(3000);
    navigationForward(info);
  }

  /**
   * Show message.
   *
   * @param message
   */
  public void showMessage(String message) {
    showMessage(message, AlertType.INFO);
  }

}
