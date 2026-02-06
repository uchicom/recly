// (C) 2026 uchicom
package com.uchicom.recly.recorder;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

public class AudioRecorder implements Callable<File> {
  volatile boolean recording = true;
  public AudioFormat audioFormat;
  TargetDataLine line;
  AudioWriter writer;
  Future<File> futureRecorder;

  public AudioRecorder(TargetDataLine line, AudioWriter writer, ExecutorService executorService) {
    this.line = line;
    audioFormat = line.getFormat();
    this.writer = writer;
    futureRecorder = executorService.submit(this);
  }

  @Override
  public File call() throws Exception {
    line.start();

    var buffer = new byte[4096];

    // 最初の読み込みで開始時間を記録
    var n = line.read(buffer, 0, buffer.length);
    var audioStart = System.nanoTime();
    write(buffer, n);

    while (recording) {
      n = line.read(buffer, 0, buffer.length);
      write(buffer, n);
    }

    line.stop();
    line.flush();
    line.close();
    writer.stop(audioStart);

    System.out.println("AudioRecorder finished.");
    return writer.futureWriter.get();
  }

  void write(byte[] buffer, int length) {
    try {
      writer.queue.put(Arrays.copyOfRange(buffer, 0, length));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public File stop() throws InterruptedException, ExecutionException {
    recording = false;
    return futureRecorder.get();
  }
}
