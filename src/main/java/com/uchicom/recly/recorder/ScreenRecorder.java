// (C) 2026 uchicom
package com.uchicom.recly.recorder;

import com.uchicom.recly.recorder.dto.ScreenDto;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ScreenRecorder implements Callable<Long> {
  volatile boolean recording = true;
  Robot robot;
  Rectangle area;
  ScreenWriter writer;
  Future<Long> futureRecorder;

  public ScreenRecorder(ScreenWriter writer, ExecutorService executorService) throws AWTException {
    this.robot = new Robot();
    area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    this.writer = writer;
    futureRecorder = executorService.submit(this);
  }

  @Override
  public Long call() throws Exception {
    while (recording) {
      var image = robot.createScreenCapture(area);
      var recodingTime = System.nanoTime();
      write(image, recodingTime);
    }

    writer.stop();
    System.out.println("ScreenRecorder finished.");
    return writer.futureWriter.get();
  }

  void write(BufferedImage image, long recordingTime) {
    try {
      writer.queue.put(new ScreenDto(image, recordingTime));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public Long stop() throws InterruptedException, ExecutionException {
    recording = false;
    return futureRecorder.get();
  }
}
