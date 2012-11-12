package com.nokia.photoshoot.views;

import com.nokia.photoshoot.MainMIDlet;
import java.io.IOException;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

/**
 * Camera component.
 *
 * Full Touch Base layout
 * http://www.developer.nokia.com/Resources/Library/Full_Touch/#!essentials/base-layout.html
 *
 * @author Aniwat Keereena
 * @since 10/11/2012
 */
public class CameraComponent {

  private final MainMIDlet midlet;
  private final Canvas canvas;
  private VideoControl videoControl; // Controls the display of video
  private Player player; // Control the rendering of media data

  public CameraComponent(MainMIDlet midlet, Canvas canvas) {
    this.midlet = midlet;
    this.canvas = canvas;
  }

  /**
   * Start player.
   */
  synchronized public void start() {
    try {
      player = Manager.createPlayer("capture://image");
      player.realize();

      // Grab the video control and set it to the current display.
      videoControl = (VideoControl) (player.getControl("VideoControl"));
      if (videoControl != null) {
        // Direct video to canvas
        videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, canvas);
        videoControl.setDisplaySize(240, 296);
        videoControl.setDisplayLocation(0, 0);
        player.start();
        videoControl.setVisible(true);
      } else {
        discardPlayer();
        midlet.showMessage("Unsupported: Can't get video control", AlertType.ERROR);
      }
    } catch (IOException ioe) {
      discardPlayer();
      midlet.showMessage("IOException: " + ioe.getMessage(), AlertType.ERROR);
    } catch (MediaException me) {
      discardPlayer();
      midlet.showMessage("MediaException: " + me.getMessage(), AlertType.ERROR);
    } catch (SecurityException se) {
      discardPlayer();
      midlet.showMessage("SecurityException: " + se.getMessage(), AlertType.ERROR);
    }
  }

  /**
   * Stop player
   */
  synchronized public void stop() {
    if (player != null) {
      try {
        videoControl.setVisible(false);
        player.stop();
        discardPlayer();
      } catch (MediaException me) {
        midlet.showMessage("MediaException: " + me.getMessage(), AlertType.ERROR);
      }
    }
  }

  /**
   * Discard player
   */
  private void discardPlayer() {
    if (player != null) {
      player.deallocate();
      player.close();
      player = null;
    }

    if (videoControl != null) {
      videoControl = null;
    }
  }

  /**
   * Takes a snapshot.
   *
   * @return Image taken by the camera
   */
  public Image getSnapshot() {
    byte[] imageBytes = null;
    try {
      imageBytes = videoControl.getSnapshot(null);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      stop();
    }

    if (imageBytes == null || imageBytes.length == 0) {
      return null;
    } else {
      return Image.createImage(imageBytes, 0, imageBytes.length);
    }
  }

}
