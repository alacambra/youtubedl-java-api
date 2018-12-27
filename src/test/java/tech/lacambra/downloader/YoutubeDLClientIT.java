package tech.lacambra.downloader;

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
    String target = " https://www.youtube.com/watch?v=BbrfdBFpjac";
    CountDownLatch latch = new CountDownLatch(1);

    Flowable<ProgressStep> progress = new YoutubeDLClient()
        .options()
        .extractAudio()
        .audioQuality(AudioOption.Quality.q128K)
        .audioFormat(AudioOption.Format.mp3)
        .execute(target)
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
  }

  private void countDown(CountDownLatch latch) {
    System.out.println("counting down: " + latch.getCount());
    latch.countDown();
  }
}
