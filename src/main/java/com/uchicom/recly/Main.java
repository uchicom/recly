// (C) 2026 uchicom
package com.uchicom.recly;

import com.uchicom.recly.factory.di.DIFactory;
import com.uchicom.recly.ui.ControlFrame;
import javax.swing.SwingUtilities;

/**
 * メイン処理.
 *
 * @author uchicom: Shigeki Uchiyama
 */
public class Main {
  /**
   * @param args
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          var main = DIFactory.main();
          main.controlFrame.pack();
          main.controlFrame.setVisible(true);
        });
  }

  private final ControlFrame controlFrame;

  public Main(ControlFrame controlFrame) {
    this.controlFrame = controlFrame;
  }
}
