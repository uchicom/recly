// (C) 2026 uchicom
package com.uchicom.recly.recorder.dto;

import java.awt.image.BufferedImage;

public class ScreenDto {
  public final BufferedImage bufferedImage;
  public final Long nanoTime;

  public ScreenDto(BufferedImage bufferedImage, long nanoTime) {
    this.bufferedImage = bufferedImage;
    this.nanoTime = nanoTime;
  }
}
