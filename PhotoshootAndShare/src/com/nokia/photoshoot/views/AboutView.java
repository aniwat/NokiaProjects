package com.nokia.photoshoot.views;

import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 * About view.
 *
 * @author Aniwat Keereena
 * @since 23/10/2012
 */
public class AboutView extends Form implements CommandListener {

  private final MainMIDlet midlet;

  public AboutView(MainMIDlet midlet) {
    super("About");
    append("Photoshoot & Share\n");
    append("Version: 1.0\n");
    append("Mega Genius Co., Ltd.");

    this.midlet = midlet;

    addCommand(Commands.BACK);
    setCommandListener(this);
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.BACK) {
      midlet.navigationBack();
    }
  }

}
