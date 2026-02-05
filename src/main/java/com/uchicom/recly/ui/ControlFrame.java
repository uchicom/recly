// (C) 2026 uchicom
package com.uchicom.recly.ui;

import com.uchicom.ui.ResumeFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ControlFrame extends ResumeFrame {

  public ControlFrame() {
    super(new File("conf/recly.properties"), "recly");
    initComponents();
  }

  void initComponents() {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    var basePanel = new JPanel();
    basePanel.setLayout(new GridLayout(2, 1));
    basePanel.add(
        new JButton(
            new AbstractAction("REC") {
              public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ControlFrame.this, "REC");
              }
            }));
    basePanel.add(
        new JButton(
            new AbstractAction("STOP") {
              public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ControlFrame.this, "STOP");
              }
            }));
    add(basePanel);
  }
}
