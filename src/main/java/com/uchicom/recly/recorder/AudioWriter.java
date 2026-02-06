// (C) 2026 uchicom
package com.uchicom.recly.recorder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AudioWriter implements Callable<File> {

  String key;
  final byte[] END = new byte[0];
  final BlockingQueue<byte[]> queue = new ArrayBlockingQueue<>(32);
  Future<File> futureWriter;
  Long audioStart;

  public AudioWriter(String key, ExecutorService executorService) {
    this.key = key;
    futureWriter = executorService.submit(this);
  }

  File createRawFile() {
    return new File(key + "__" + System.nanoTime() + ".raw");
  }

  @Override
  public File call() throws Exception {
    var file = createRawFile();
    try (var out = new FileOutputStream(file)) {
      while (true) {
        byte[] data = queue.take();
        if (data == END) break;
        out.write(data);
        out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("AudioWriter finished.");

    return rename(audioStart, file);
  }

  void stop(long audioStart) {
    this.audioStart = audioStart;
    try {
      queue.put(END);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  File rename(Long audioStart, File recodingFile) {
    var newFile = new File(key + "_" + audioStart + ".raw");
    recodingFile.renameTo(newFile);
    return newFile;
  }
}
