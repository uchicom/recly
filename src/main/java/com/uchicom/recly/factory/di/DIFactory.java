// (C) 2026 uchicom
package com.uchicom.recly.factory.di;

import com.uchicom.recly.Main;
import com.uchicom.recly.ui.ControlFrame;

public class DIFactory {
  public static Main main() {
    return new Main(controlFrame());
  }

  static ControlFrame controlFrame() {
    return new ControlFrame(ServiceFactory.audioService());
  }
}
