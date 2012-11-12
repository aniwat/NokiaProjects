package com.nokia.photoshoot.utils;

import com.nokia.mid.ui.CategoryBar;

public class CategoryBarUtils {

  /**
   * Wrapper for com.nokia.mid.ui.CategoryBar#setElementListener method.
   */
  public static void setListener(final CategoryBar bar, final ElementListener listener) {

    bar.setElementListener(listener == null ? null : new com.nokia.mid.ui.ElementListener() {
      public void notifyElementSelected(CategoryBar bar, int selectedIndex) {
        listener.notifyElementSelected(bar, selectedIndex);
      }

    });
  }

  /**
   * ElementListener interface for devices which don't support com.nokia.mid.ui.ElementListener.
   */
  public interface ElementListener {

    public void notifyElementSelected(CategoryBar categoryBar, int selectedIndex);

  }

}
