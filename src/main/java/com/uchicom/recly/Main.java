// (C) 2026 uchicom
package com.uchicom.recly;

import java.awt.Dimension;
import javax.swing.JFrame;
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
          var remonFrame = new JFrame();
          remonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          remonFrame.setSize(new Dimension(800, 600));
          remonFrame.setVisible(true);
        });
  }
}
