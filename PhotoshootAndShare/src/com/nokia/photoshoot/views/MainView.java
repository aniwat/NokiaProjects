package com.nokia.photoshoot.views;

import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.IconCommand;
import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.CategoryBarUtils;
import com.nokia.photoshoot.utils.Commands;
import com.nokia.photoshoot.utils.ImageLoader;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;

/**
 * Main view.
 *
 * @author Aniwat Keereena
 * @since 23/10/2012
 */
public class MainView extends Form implements CommandListener, CategoryBarUtils.ElementListener {

  private final MainMIDlet midlet;
  private final String[] VIEW_NAMES = {"Home", "Camera", "Photos", "Options"};
  private final CategoryBar categoryBar;
  private final AboutView aboutView;
  private final CameraView cameraView;
  private final PhotosView photosView;
  private final OptionsView optionsView;

  public MainView(MainMIDlet midlet) {
    super("Photoshoot & Share");

    append("Welcome to Photoshoot & Share");

    this.midlet = midlet;

    categoryBar = createCategoryBar();
    categoryBar.setVisibility(true);
    categoryBar.setHighlightColour(midlet.getDisplay().getColor(Display.COLOR_HIGHLIGHTED_BACKGROUND));

    addCommand(Commands.ABOUT);
    addCommand(Commands.BACK);
    setCommandListener(this);

    CategoryBarUtils.setListener(categoryBar, this);

    // Initial views
    aboutView = new AboutView(midlet);
    cameraView = new CameraView(midlet);
    photosView = new PhotosView(midlet);
    optionsView = new OptionsView(midlet);
  }

  private CategoryBar createCategoryBar() {
    Vector commands = new Vector();

    Image homeImage = ImageLoader.load("/categorybar_home.png");
    Image cameraImage = ImageLoader.load("/categorybar_camera.png");
    Image photosImage = ImageLoader.load("/categorybar_photos.png");
    Image optionsImage = ImageLoader.load("/categorybar_options.png");

    commands.addElement(new IconCommand(VIEW_NAMES[0], homeImage, null, Command.SCREEN, 1));
    commands.addElement(new IconCommand(VIEW_NAMES[1], cameraImage, null, Command.SCREEN, 1));
    commands.addElement(new IconCommand(VIEW_NAMES[2], photosImage, null, Command.SCREEN, 1));
    commands.addElement(new IconCommand(VIEW_NAMES[3], optionsImage, null, Command.SCREEN, 1));


    IconCommand[] iconCommands = new IconCommand[VIEW_NAMES.length];
    for (int i = 0; i < VIEW_NAMES.length; i++) {
      iconCommands[i] = (IconCommand) commands.elementAt(i);
    }

    return new CategoryBar(iconCommands, true);
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.ABOUT) {
      midlet.navigationForward(aboutView);
      setCategoryBarInvisible();
    } else if (command == Commands.BACK || command == Commands.EXIT) {
      midlet.navigationBack();
      midlet.destroyApp(true);
    }
  }

  public void notifyElementSelected(CategoryBar categoryBar, int selectedIndex) {
    if (selectedIndex == 0) {
      midlet.navigationCurrent(this);
    } else if (selectedIndex == 1) {
      setCategoryBarInvisible();
      cameraView.start();
      midlet.navigationForward(cameraView);
    } else if (selectedIndex == 2) {
      setCategoryBarInvisible();
      photosView.initialize();
      midlet.navigationForward(photosView);
    } else if (selectedIndex == 3) {
      midlet.navigationCurrent(optionsView);
    } else if (selectedIndex == 4) {
      midlet.navigationBack();
      midlet.destroyApp(true);
    }
  }

  public void setCategoryBarVisible() {
    categoryBar.setVisibility(true);
  }

  public void setCategoryBarInvisible() {
    categoryBar.setVisibility(false);
  }

  public void setCategoryBarElementSelected(int selected) {
    categoryBar.setSelectedIndex(selected);
  }

}
