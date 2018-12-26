package tech.lacambra.downloader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionBuilderTest {
  OptionBuilder cut;

  @BeforeEach
  void setUp() {
    cut = new OptionBuilder(null);
  }

  @Test
  void build() {

    String opts = cut
        .afterDate(LocalDate.of(2018, 10, 23))
        .extractAudio()
        .audioFormat(AudioOption.Format.mp3)
        .audioQuality(AudioOption.Quality.q128K)
        .build();
    assertEquals("--dateafter 2018-10-23 --extract-audio  --audio-format mp3 --audio-quality 128K", opts);

  }
}