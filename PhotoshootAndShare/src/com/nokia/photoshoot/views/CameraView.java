package com.nokia.photoshoot.views;

import com.nokia.mid.ui.VirtualKeyboard;
import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import com.nokia.photoshoot.utils.ImageLoader;
import javax.microedition.lcdui.*;

public class CameraView extends Canvas implements CommandListener, GestureListener {

  private final MainMIDlet midlet;
  private final CameraPreviewView cameraPreviewView;
  private final CameraPreviewView2 cameraPreviewView2;
  private final CameraComponent cameraComponent;
  private Image snapshotImage;
  private GestureInteractiveZone snapshotGiz;

  public CameraView(MainMIDlet midlet) {
    setTitle("Capture");

    // Hide virtual keyboard
    VirtualKeyboard.hideOpenKeypadCommand(true);

    this.midlet = midlet;

    // addCommand(Commands.CAMERA_SNAPSHOT);
    addCommand(Commands.BACK);
    setCommandListener(this);

    snapshotImage = ImageLoader.load("/snapshot.png");

    // Create a GestureInteractiveZone for snapshot.
    snapshotGiz = new GestureInteractiveZone(GestureInteractiveZone.GESTURE_RECOGNITION_END);
    snapshotGiz.setRectangle((getWidth() / 2) - (snapshotImage.getWidth() / 2), getHeight() - snapshotImage.getHeight() - 6, snapshotImage.getWidth(), snapshotImage.getHeight());

    // Register for Gesture events.
    GestureRegistrationManager.register(this, snapshotGiz);
    GestureRegistrationManager.setListener(this, this);

    cameraPreviewView = new CameraPreviewView(midlet);
    cameraPreviewView2 = new CameraPreviewView2(midlet);
    cameraComponent = new CameraComponent(midlet, this);
  }

  public void start() {
    cameraComponent.start();
  }

  public void paint(Graphics g) {
    g.setColor(0x000000);
    g.fillRect(0, 0, getWidth(), getHeight());

    g.drawImage(snapshotImage, (getWidth() / 2) - (snapshotImage.getWidth() / 2), getHeight() - snapshotImage.getHeight() - 6, Graphics.LEFT | Graphics.TOP);

    // g.setColor(0xFF0000);
    // g.fillRect((getWidth() / 2) - (snapshotImage.getWidth() / 2), getHeight() - snapshotImage.getHeight() - 3, snapshotImage.getWidth(), snapshotImage.getHeight());
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.CAMERA_SNAPSHOT) {
      cameraPreviewView.setImage(cameraComponent.getSnapshot());
      cameraComponent.stop();
      midlet.navigationForward(cameraPreviewView);
    } else if (command == Commands.BACK) {
      cameraComponent.stop();
      midlet.navigationBack();
    }
  }

  public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
    if (gestureInteractiveZone == snapshotGiz) {
      //cameraPreviewView.setImage(cameraComponent.getSnapshot());
      cameraPreviewView2.setImage(cameraComponent.getSnapshot());
      cameraComponent.stop();
      midlet.navigationForward(cameraPreviewView);
    }
  }

}
