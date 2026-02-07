// (C) 2026 uchicom
package com.uchicom.recly.service;

import com.uchicom.recly.recorder.AudioRecorder;
import com.uchicom.recly.recorder.AudioWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class AudioService {

  public List<Mixer.Info> getMixerInfo() throws LineUnavailableException {
    return Stream.of(AudioSystem.getMixerInfo())
        .filter(
            info -> {
              Mixer mixer = AudioSystem.getMixer(info);

              return mixer.getTargetLineInfo(new Line.Info(TargetDataLine.class)).length > 0;
            })
        .collect(Collectors.toList());
  }

  public AudioRecorder createAudioRecorder(
      Mixer.Info mixerInfo, String key, ExecutorService executorService)
      throws LineUnavailableException {
    var line = opendDataLine(mixerInfo);
    return new AudioRecorder(line, new AudioWriter(key, executorService), executorService);
  }

  public TargetDataLine opendDataLine(Mixer.Info mixerInfo) throws LineUnavailableException {

    var mixer = AudioSystem.getMixer(mixerInfo);

    var lineInfo = new DataLine.Info(TargetDataLine.class, null);

    var line = (TargetDataLine) mixer.getLine(lineInfo);
    line.open();
    return line;
  }

  public File pcmToWav(File pcmFile, AudioFormat audioFormat) throws IOException {
    // PCMをWAVに変換する実装

    try (var fis = new FileInputStream(pcmFile);
        var ais = createAudioInputStream(fis, pcmFile.length(), audioFormat)) {
      var wavFile = new File(pcmFile.getParent(), pcmFile.getName() + ".wav");
      AudioSystem.write(ais, Type.WAVE, wavFile);
      return wavFile;
    }
  }

  AudioInputStream createAudioInputStream(FileInputStream fis, long fileSize, AudioFormat format)
      throws IOException {
    var ais = new AudioInputStream(fis, format, fileSize / format.getFrameSize());
    return ais;
  }
}
