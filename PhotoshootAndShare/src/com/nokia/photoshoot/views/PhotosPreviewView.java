package com.nokia.photoshoot.views;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import com.nokia.mid.ui.orientation.Orientation;
import com.nokia.mid.ui.orientation.OrientationListener;
import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.*;

/**
 * This class displays a selected image centered in the screen. This class
 * implements a GestureListener which notifies the MIDlet of gesture events
 * associated with the UI element. This class also implements
 * OrientationListener to detect a change in the orientation of the device's
 * display
 */
class PhotosPreviewView extends Canvas implements CommandListener, OrientationListener, GestureListener {

  private final MainMIDlet midlet;
  private final Ticker ticker;
  private Image currentImage;
  int imageWidth;
  int imageHeight;
  private int mouseDownX;
  private int mouseDownY;
  private int deltaX;
  private int deltaY;
  private int posX;
  private int posY;
  int rgbImageData[];
  int srcWidth;
  int srcHeight;
  float scaleIndex = 1;

  public PhotosPreviewView(MainMIDlet midlet) {
    setTitle("Photos");

    this.midlet = midlet;

    currentImage = null;

    ticker = new Ticker("Photo");
    setTicker(ticker);

    addCommand(Commands.BACK);
    setCommandListener(this);

    // Create a GestureInteractiveZone for image.
    GestureInteractiveZone imageGiz = new GestureInteractiveZone(GestureInteractiveZone.GESTURE_PINCH);
    imageGiz.setRectangle(0, 0, getWidth(), getHeight());

    // Register for Gesture events.
    GestureRegistrationManager.register(this, imageGiz);
    GestureRegistrationManager.setListener(this, this);
  }

  public boolean displayImage(String imgName) {
    try {
      FileConnection fileConn = (FileConnection) Connector.open(imgName, Connector.READ);

      if (rgbImageData != null) {
        rgbImageData = null;
      }

      InputStream fis = fileConn.openInputStream();
      int overallSize = (int) fileConn.fileSize();

      byte[] imageData = new byte[overallSize];

      int readAmount = fis.read(imageData, 0, overallSize);

      fis.close();
      fileConn.close();

      //fis = null;
      //fileConn = null;

      ticker.setString("Image Viewer:" + imgName);

      currentImage = Image.createImage(imageData, 0, overallSize);
      imageData = null;

      srcWidth = imageWidth = currentImage.getWidth();
      srcHeight = imageHeight = currentImage.getHeight();

      scaleIndex = 1;

      repaint();
    } catch (IOException ioe) {
      midlet.showMessage("IOException: " + ioe.getMessage(), AlertType.ERROR);
      return false;
    } catch (Exception ex) {
      midlet.showMessage("Exception: " + ex.getMessage(), AlertType.ERROR);
      return false;
    } catch (OutOfMemoryError ome) {
      midlet.showMessage("OutOfMemoryError: File is too large to display", AlertType.ERROR);
      return false;
    } catch (Error e) {
      midlet.showMessage("Error: Failed to display this file", AlertType.ERROR);
      return false;
    }
    return true;
  }

  protected void paint(Graphics g) {
    g.setColor(0x000000);
    g.fillRect(0, 0, getWidth(), getHeight());

    setImagePlacementPoint();

    System.out.println("Pos X:" + posX + " Y:" + posY);

    if (currentImage != null) {
      g.drawImage(currentImage, posX, posY, Graphics.HCENTER | Graphics.VCENTER);
    } else {
      // If no image is available display a message
      g.setColor(0x00FFFFFF);
      g.drawString("No image", posX, posY, Graphics.HCENTER | Graphics.BASELINE);
    }
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.BACK) {
      midlet.navigationBack();
    }
  }

  public void gestureAction(Object container, GestureInteractiveZone gestureInteractiveZone, GestureEvent gestureEvent) {
    switch (gestureEvent.getType()) {
      case GestureInteractiveZone.GESTURE_PINCH:
        if (gestureEvent.getPinchDistanceChange() < 0) {
          scaleImage(-0.1f);
        } else if (gestureEvent.getPinchDistanceChange() > 0) {
          scaleImage(0.1f);
        }
        repaint();
        break;

      default:
        break;
    }
  }

  protected void pointerPressed(int x, int y) {
    mouseDownX = x;
    mouseDownY = y;
  }

  protected void pointerReleased(int x, int y) {
    deltaX = 0;
    deltaY = 0;
  }

  protected void pointerDragged(int x, int y) {
    deltaX = x - mouseDownX;
    deltaY = y - mouseDownY;
    mouseDownX = x;
    mouseDownY = y;
    repaint();
  }

  /**
   * Orientation is supported for Java Runtime 2.0.0 for Series 40 onwards.
   * Called when display's orientation has changed.
   */
  public void displayOrientationChanged(int newDisplayOrientation) {
    // Change MIDlet UI orientation
    Orientation.setAppOrientation(newDisplayOrientation);
  }

  private void setImagePlacementPoint() {
    // This needs to be taken each time, since the values will be chaged when
    // user tilt the phone to potrait mode to landscape mode.
    int canvasWidth = getWidth();
    int canvasHeight = getHeight();

    if (imageWidth > canvasWidth && deltaX != 0) {
      posX += deltaX;
      if (posX < (canvasWidth - imageWidth / 2)) {
        posX = canvasWidth - imageWidth / 2;
      } else if (posX > (imageWidth / 2)) {
        posX = (imageWidth / 2);
      }
    } else {
      posX = canvasWidth / 2;
    }

    if (imageHeight > canvasHeight && deltaY != 0) {
      posY += deltaY;
      if (posY < (canvasHeight - imageHeight / 2)) {
        posY = canvasHeight - imageHeight / 2;
      } else if (posY > (imageHeight / 2)) {
        posY = (imageHeight / 2);
      }
    } else {
      posY = canvasHeight / 2;
    }
  }

  /**
   * Scales the current image
   */
  private void scaleImage(float scale) {
    try {
      scaleIndex += scale;

      if (rgbImageData == null) {
        rgbImageData = new int[srcWidth * srcHeight];
        currentImage.getRGB(rgbImageData, 0, imageWidth, 0, 0, imageWidth, imageHeight);
      }

      if (scaleIndex < 0.1f) {
        scaleIndex = 0.1f;
      }

      int newImageWidth = (int) (srcWidth * scaleIndex);
      int newImageHeight = (int) (srcHeight * scaleIndex);

      int rgbImageScaledData[] = new int[newImageWidth * newImageHeight];

      // Calculations and bit shift operations to optimize the for loop
      int tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
      int tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

      int i = 0;

      for (int y = 0; y < newImageHeight; y++) {
        for (int x = 0; x < newImageWidth; x++) {
          rgbImageScaledData[i++] = rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16)) + ((x * tempScaleRatioWidth) >> 16)];
        }
      }

      // Create an RGB image from rgbImageScaledData array
      currentImage = Image.createRGBImage(rgbImageScaledData, newImageWidth, newImageHeight, true);
      imageWidth = newImageWidth;
      imageHeight = newImageHeight;
    } catch (OutOfMemoryError ome) {
      scaleIndex -= scale;
      ome.printStackTrace();
      midlet.showMessage("OutOfMemoryError: " + ome.getMessage(), AlertType.ERROR);
    }
  }

}
