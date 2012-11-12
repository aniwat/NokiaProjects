/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.photoshoot.views;

import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Aniwat Keereena
 */
public class CameraPreviewView2 extends Form implements CommandListener {

  private final MainMIDlet midlet;

  public CameraPreviewView2(MainMIDlet midlet) {
    super("Preview");

    this.midlet = midlet;

    addCommand(Commands.BACK);
    setCommandListener(this);
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.BACK) {
      midlet.navigationBack();
    }
  }

  public void setImage(Image image) {
    deleteAll();
    append(image);
  }

}
