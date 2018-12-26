package tech.lacambra.downloader;

public class Downloader {

  public static void main(String[] args) {

    final YoutubeDL youtubeDL;
    youtubeDL = new YoutubeDL();

    Thread t = new Thread(() -> {
      String o = new OptionBuilder()
          .extractAudio()
          .audioQuality(AudioOption.Quality.q128K)
          .audioFormat(AudioOption.Format.mp3)
          .build();

      String r = youtubeDL.setOptions(o).execute("https://www.youtube.com/watch?v=BbrfdBFpjac");
//      System.out.println(r);
    });
    t.setDaemon(false);
    t.start();
  }
}
