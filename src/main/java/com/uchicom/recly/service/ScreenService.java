// (C) 2026 uchicom
package com.uchicom.recly.service;

import com.uchicom.recly.recorder.ScreenRecorder;
import com.uchicom.recly.recorder.ScreenWriter;
import java.awt.AWTException;
import java.util.concurrent.ExecutorService;

public class ScreenService {

  public ScreenRecorder createScreenRecorder(ExecutorService executorService) throws AWTException {
    return new ScreenRecorder(new ScreenWriter(executorService), executorService);
  }
}
