package downloader;

import downloader.tech.lacambra.downloader.client.AudioOption;
import downloader.tech.lacambra.downloader.client.ProgressStep;
import downloader.tech.lacambra.downloader.client.YoutubeDLClient;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class YoutubeDLClientIT {

  @Test
  void downloadTest() throws InterruptedException {

    AtomicInteger i = new AtomicInteger(-100);
    String target = " https://www.youtube.com/watch?v=la4zh4QjO00&feature=youtu.be";
    CountDownLatch latch = new CountDownLatch(1);

    Flowable<ProgressStep> progress = new YoutubeDLClient()
        .options()
        .extractAudio(true)
        .audioQuality(AudioOption.Quality.q128K)
        .audioFormat(AudioOption.Format.mp3)
        .execute(target, YoutubeDLClient.TARGET_FOLDER)
        .observeOn(Schedulers.computation());

    Disposable d2 = progress
        .subscribe(System.out::println);

    Disposable d1 =
        progress
            .filter(progressStep -> progressStep.getExitCode().isPresent())
            .subscribe(progressStep -> progressStep.getExitCode().ifPresent(i::set), System.err::println, () -> {
              System.out.println("done:" + i);
              this.countDown(latch);
            });


    latch.await();
    d1.dispose();
    d2.dispose();

    sleep(10000);
  }

  private static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void countDown(CountDownLatch latch) {
    System.out.println("counting down: " + latch.getCount());
    latch.countDown();
  }

  @Test
  public void getNameTest() throws InterruptedException {
//    String target = " https://www.youtube.com/watch?v=la4zh4QjO00&feature=youtu.be";
    String target = "https://www.youtube.com/playlist?list=PLYgCn1ybbmZqoy-vBLWKJ5aJdv28umH72";
    CountDownLatch latch = new CountDownLatch(1);
    new YoutubeDLClient()
        .options().getFileName().execute(target, YoutubeDLClient.TARGET_FOLDER)
        .subscribe(progressStep -> {
          System.out.println(progressStep);
          System.out.println(progressStep.getLine());
        }, System.err::println, latch::countDown);

    latch.await();
  }
}
