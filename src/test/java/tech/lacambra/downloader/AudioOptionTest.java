package tech.lacambra.downloader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class AudioOptionTest {

  @Test
  public void testFormatting() {
    Assertions.assertEquals(LocalDate.of(2010, 10, 30), LocalDate.from(AudioOption.FORMATTER.parse("20101030")));
  }
}
