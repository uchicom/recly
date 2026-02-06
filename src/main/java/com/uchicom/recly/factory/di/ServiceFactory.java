// (C) 2026 uchicom
package com.uchicom.recly.factory.di;

import com.uchicom.recly.service.AudioService;

public class ServiceFactory {
  public static AudioService audioService() {
    return new AudioService();
  }
}
