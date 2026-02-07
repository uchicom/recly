// (C) 2026 uchicom
package com.uchicom.recly.recorder;

import com.uchicom.recly.recorder.dto.ScreenDto;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class ScreenWriter implements Callable<Long> {

  final ScreenDto END = new ScreenDto(null, 0);
  final BlockingQueue<ScreenDto> queue = new ArrayBlockingQueue<>(32);
  Future<Long> futureWriter;

  public ScreenWriter(ExecutorService executorService) {
    futureWriter = executorService.submit(this);
  }

  File createJpgFile(long nanoTime) {
    return new File("data/screen_" + nanoTime + ".jpg");
  }

  @Override
  public Long call() throws Exception {
    long count = 0;
    var writer = ImageIO.getImageWritersByFormatName("jpeg").next();
    var param = writer.getDefaultWriteParam();

    // 品質指定
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(0.5f); // 0.0 ～ 1.0
    try {

      while (true) {
        var data = queue.take();
        if (data == END) break;
        var file = createJpgFile(data.nanoTime);

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(file)) {
          writer.setOutput(ios);
          writer.write(null, new IIOImage(data.bufferedImage, null, null), param);
        }
        count++;
      }
    } finally {
      writer.dispose();
    }
    System.out.println("ScreenWriter finished.");
    return count;
  }

  void stop() {
    try {
      queue.put(END);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
