package com.nokia.photoshoot.views;

import com.nokia.mid.ui.VirtualKeyboard;
import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import javax.microedition.lcdui.*;

public class CameraPreviewView extends Canvas implements CommandListener {

  private final MainMIDlet midlet;
  private Image image;

  public CameraPreviewView(MainMIDlet midlet) {
    setTitle("Preview");

    // Hide virtual keyboard
    VirtualKeyboard.hideOpenKeypadCommand(true);

    this.midlet = midlet;

    addCommand(Commands.BACK);
    setCommandListener(this);
  }

  public void paint(Graphics g) {
    g.setColor(0x000000);
    g.fillRect(0, 0, getWidth(), getHeight());
    if (image != null) {
      g.drawImage(image, getWidth() / 2, getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
    }
  }

  public void setImage(byte[] pngImage) {
    this.image = Image.createImage(pngImage, 0, pngImage.length);
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.BACK) {
      midlet.navigationBack();
    }
  }

}
