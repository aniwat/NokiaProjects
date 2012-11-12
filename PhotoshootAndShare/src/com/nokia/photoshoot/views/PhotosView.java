package com.nokia.photoshoot.views;

import com.nokia.photoshoot.MainMIDlet;
import com.nokia.photoshoot.utils.Commands;
import com.nokia.photoshoot.utils.ImageLoader;
import java.io.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;

class PhotosView extends List implements CommandListener { //, FileSystemListener {

  private final MainMIDlet midlet;
  private final PhotosPreviewView photosPreviewView;
  private final Ticker ticker;
  private final OperationsQueue queue;
  private final static Image ROOT_IMAGE = ImageLoader.load("/root.png");
  private final static Image FOLDER_IMAGE = ImageLoader.load("/folder.png");
  private final static Image FILE_IMAGE = ImageLoader.load("/file.png");
  private final static String FILE_SEPARATOR = (System.getProperty("file.separator") != null) ? System.getProperty("file.separator") : "/";
  private final static String UPPER_DIR = "..";
  private final static int INIT_OPERATION = 1;
  private final static int OPEN_OPERATION = 2;
  private Vector rootsList;
  private FileConnection fileConnection; // Stores the current root, if null we are showing all the roots

  public PhotosView(MainMIDlet midlet) {
    super("Photos", List.IMPLICIT);

    this.midlet = midlet;

    ticker = new Ticker("Photos");
    setTicker(ticker);

    queue = new OperationsQueue();
    rootsList = new Vector();

    setSelectCommand(Commands.OPEN_ITEM);
    addCommand(Commands.OPEN_ITEM);
    addCommand(Commands.BACK);
    setCommandListener(this);

    photosPreviewView = new PhotosPreviewView(midlet);
  }

  void initialize() {
    queue.enqueueOperation(new ImageViewerOperations(INIT_OPERATION));
//    FileSystemRegistry.addFileSystemListener(PhotosView.this);
  }

//  void stop() {
//    queue.abort();
//    FileSystemRegistry.removeFileSystemListener(this);
//  }
  public void commandAction(Command command, Displayable displayable) {
    if (command == Commands.OPEN_ITEM) {
      queue.enqueueOperation(new ImageViewerOperations(OPEN_OPERATION));
    } else if (command == Commands.BACK) {
      midlet.navigationBack();
    }
  }

//  // Listen for changes in the roots
//  public void rootChanged(int state, String rootName) {
//    queue.enqueueOperation(new ImageViewerOperations(INIT_OP));
//  }
  private void displayAllRoots() {
    ticker.setString("Photos - [Roots]");
    deleteAll();
    Enumeration roots = rootsList.elements();
    while (roots.hasMoreElements()) {
      String root = (String) roots.nextElement();
      root = root.replace('/', FILE_SEPARATOR.charAt(0));
      append(root.substring(1), ROOT_IMAGE);
    }
    fileConnection = null;
  }

  private void loadRoots() {
    if (!rootsList.isEmpty()) {
      rootsList.removeAllElements();
    }
    try {
      Enumeration roots = FileSystemRegistry.listRoots();
      while (roots.hasMoreElements()) {
        rootsList.addElement("/" + (String) roots.nextElement());
      }
    } catch (Throwable e) {
      midlet.showMessage(e.getMessage(), AlertType.ERROR);
    }
  }

  private void openSelected() {
    int selectedIndex = getSelectedIndex();
    if (selectedIndex >= 0) {
      String selectedFile = getString(selectedIndex);
      if (selectedFile.endsWith(FILE_SEPARATOR)) { // Directory selected
        try {
          String tmp = selectedFile.replace(FILE_SEPARATOR.charAt(0), '/');
          if (fileConnection == null) {
            fileConnection = (FileConnection) Connector.open("file:///" + tmp, Connector.READ);
          } else {
            fileConnection.setFileConnection(tmp);
          }
          displayCurrentRoot();
        } catch (IOException ioe) {
          midlet.showMessage("IOException: " + ioe.getMessage(), AlertType.ERROR);
        } catch (SecurityException se) {
          midlet.showMessage("SecurityException: " + se.getMessage(), AlertType.ERROR);
        }
      } else if (selectedFile.equals(UPPER_DIR)) { // Upper directory selected
        if (rootsList.contains(fileConnection.getPath() + fileConnection.getName())) {
          displayAllRoots();
        } else {
          try {
            fileConnection.setFileConnection(UPPER_DIR);
            displayCurrentRoot();
          } catch (IOException ioe) {
            midlet.showMessage("IOException: " + ioe.getMessage(), AlertType.ERROR);
          }
        }
      } else { // File selected
        String url = fileConnection.getURL() + selectedFile;
        if (photosPreviewView.displayImage(url) == true) {
          midlet.navigationForward(photosPreviewView);
        }
      }
    }
  }

  private void displayCurrentRoot() {
    try {
      ticker.setString("Path - [" + fileConnection.getURL() + "]");
      // Open the root
      deleteAll();
      append(UPPER_DIR, FOLDER_IMAGE);

      // list all dirs
      Enumeration listOfDirs = fileConnection.list("*", false);
      while (listOfDirs.hasMoreElements()) {
        String currentDir = ((String) listOfDirs.nextElement());
        if (currentDir.endsWith("/")) {
          String tmp = currentDir.replace('/', FILE_SEPARATOR.charAt(0));
          append(tmp, FOLDER_IMAGE);                    // always display the platform specific seperator to the user
        }
      }

      // List all png files and dont show hidden files
      Enumeration listOfFiles = fileConnection.list("*.png", false);
      while (listOfFiles.hasMoreElements()) {
        String currentFile = (String) listOfFiles.nextElement();
        if (currentFile.endsWith(FILE_SEPARATOR)) {
          append(currentFile, FOLDER_IMAGE);
        } else {
          append(currentFile, FILE_IMAGE);
        }
      }

      listOfFiles = fileConnection.list("*.jpg", false);
      while (listOfFiles.hasMoreElements()) {
        String currentFile = (String) listOfFiles.nextElement();
        if (currentFile.endsWith(FILE_SEPARATOR)) {
          append(currentFile, FOLDER_IMAGE);
        } else {
          append(currentFile, FILE_IMAGE);
        }
      }

      listOfFiles = fileConnection.list("*.bmp", false);
      while (listOfFiles.hasMoreElements()) {
        String currentFile = (String) listOfFiles.nextElement();
        if (currentFile.endsWith(FILE_SEPARATOR)) {
          append(currentFile, FOLDER_IMAGE);
        } else {
          append(currentFile, FILE_IMAGE);
        }
      }

      // Making the top item visible.
      setSelectedIndex(0, true);
    } catch (IOException ioe) {
      midlet.showMessage("IOException: " + ioe.getMessage(), AlertType.ERROR);
    } catch (SecurityException se) {
      midlet.showMessage("SecurityException: " + se.getMessage(), AlertType.ERROR);
    }
  }

  private class ImageViewerOperations implements Operation {

    private final int operationCode;

    ImageViewerOperations(int operationCode) {
      this.operationCode = operationCode;
    }

    public void execute() {
      switch (operationCode) {
        case INIT_OPERATION:
          String initDir = System.getProperty("fileconn.dir.photos");
          loadRoots();
          if (initDir != null) {
            try {
              fileConnection = (FileConnection) Connector.open(initDir, Connector.READ);
              displayCurrentRoot();
            } catch (Exception ex) {
              midlet.showMessage("Exception: " + ex.getMessage(), AlertType.ERROR);
              displayAllRoots();
            }
          } else {
            displayAllRoots();
          }
          break;
        case OPEN_OPERATION:
          openSelected();
          break;
      }
    }

  }

}
