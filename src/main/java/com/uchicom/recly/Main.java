// (C) 2026 uchicom
package com.uchicom.recly;

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
          var frame = new ControlFrame();
          frame.pack();
          frame.setVisible(true);
        });
  }
}
