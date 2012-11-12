package com.nokia.photoshoot.utils;

import javax.microedition.lcdui.Command;

/**
 * A public store for commonly used Commands.
 */
public class Commands {

  // View Commands.
  public static final Command ABOUT = new Command(toFT("About"), Command.SCREEN, 3);
//  public static final Command ABOUT_BACK = new Command(toFT("Back"), Command.BACK, 1);
  public static final Command CAMERA_SNAPSHOT = new Command("OK", Command.OK, 1);
//  public static final Command CAMERA_BACK = new Command(toFT("Back"), Command.BACK, 1);
//  public static final Command CAMERA_PREVIEW_BACK = new Command(toFT("Back"), Command.BACK, 1);
//  public static final Command GALLERY_BACK = new Command(toFT("Back"), Command.BACK, 1);
//  public static final Command OPTIONS_BACK = new Command(toFT("Back"), Command.BACK, 1);
  public static final Command OPEN_ITEM = new Command("Open", Command.ITEM, 1);
  // Common Commands.
  public static final Command SELECT = new Command(toFT("Select"), Command.SCREEN, 1);
  public static final Command BACK = new Command(toFT("Back"), Command.BACK, 1);
  public static final Command EXIT = new Command(toFT("Back"), Command.EXIT, 1);
  public static final Command OK = new Command("OK", Command.OK, 1);
  public static final Command DONE = new Command(toFT("Done"), Command.OK, 1);
  public static final Command CANCEL = new Command(toFT("Cancel"), Command.CANCEL, 1);

  private static String toFT(String text) {
    return Compatibility.toLowerCaseIfFT(text);
  }

  private static String toFTAlert(String text) {
    return Compatibility.isFullTouch() ? text.toUpperCase() : text;
  }

}
