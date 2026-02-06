// (C) 2026 uchicom
package com.uchicom.recly.ui;

import com.uchicom.recly.recorder.AudioRecorder;
import com.uchicom.recly.service.AudioService;
import com.uchicom.ui.ResumeFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.Mixer;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ControlFrame extends ResumeFrame {

  JComboBox<Mixer.Info> mixer1Combo;
  JComboBox<Mixer.Info> mixer2Combo;
  private final AudioService audioService;
  AudioRecorder recorder1;
  AudioRecorder recorder2;
  ExecutorService executorService = Executors.newFixedThreadPool(4);

  public ControlFrame(AudioService audioService) {
    super(new File("conf/recly.properties"), "recly");
    this.audioService = audioService;
    initComponents();
  }

  void initComponents() {
    setTitle("Recly");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    var basePanel = new JPanel();
    basePanel.setLayout(new GridLayout(4, 1));
    try {
      var mixerInfoList = audioService.getMixerInfo();
      mixer1Combo = new JComboBox<>(new Vector<>(mixerInfoList));
      mixer2Combo = new JComboBox<>(new Vector<>(mixerInfoList));
    } catch (Exception e) {
      showMessage("ミキサー情報の取得に失敗しました.");
      return;
    }
    basePanel.add(mixer1Combo);
    basePanel.add(mixer2Combo);
    basePanel.add(
        new JButton(
            new AbstractAction("REC") {
              public void actionPerformed(ActionEvent e) {
                startRecording();
              }
            }));
    basePanel.add(
        new JButton(
            new AbstractAction("STOP") {
              public void actionPerformed(ActionEvent e) {
                stopRecording();
              }
            }));
    add(basePanel);

    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent evt) {
            executorService.shutdownNow();
          }
        });
  }

  void startRecording() {
    try {
      var mixer1 = (Mixer.Info) mixer1Combo.getSelectedItem();
      recorder1 = audioService.createAudioRecorder(mixer1, "mixer1", executorService);
      var mixer2 = (Mixer.Info) mixer2Combo.getSelectedItem();
      if (mixer1 == mixer2) {
        return;
      }
      recorder2 = audioService.createAudioRecorder(mixer2, "mixer2", executorService);
    } catch (Exception e) {
      showMessage("録音デバイスが利用できません。" + e.getMessage());
    }
  }

  void stopRecording() {

    try {
      var recordingFile = recorder1.stop();
      recordingFile = audioService.pcmToWav(recordingFile, recorder1.audioFormat);
      if (recorder2 != null) {
        var mixerFile = recorder2.stop();
        audioService.pcmToWav(mixerFile, recorder2.audioFormat);
      }
      showMessage(recordingFile.getName() + "を作成しました。");
    } catch (IOException e) {
      showMessage("WAV変換に失敗しました。" + e.getMessage());
    } catch (Exception e) {
      showMessage("録音停止中にエラーが発生しました。" + e.getMessage());
    }
  }

  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }
}
