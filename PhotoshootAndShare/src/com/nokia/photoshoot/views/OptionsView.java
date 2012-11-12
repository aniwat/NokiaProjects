package com.nokia.photoshoot.views;

import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 * Options view.
 *
 * @author Aniwat Keereena
 * @since 23/10/2012
 */
public class OptionsView extends Form implements CommandListener {

  private final MainMIDlet midlet;

  public OptionsView(MainMIDlet midlet) {
    super("Options");

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
