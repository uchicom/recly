// (C) 2026 uchicom
package com.uchicom.recly.factory.di;

import com.uchicom.recly.service.AudioService;
import com.uchicom.recly.service.ScreenService;

public class ServiceFactory {
  public static AudioService audioService() {
    return new AudioService();
  }

  public static ScreenService screenService() {
    return new ScreenService();
  }
}
