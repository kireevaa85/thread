package ru.kireev.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadMethodDemo {

  public static void main(String[] args) throws InterruptedException {
    log.info("starting");

    Thread thread = new Thread(
        () -> {
          boolean stop = false;
          while (!stop) {
            log.info("I am: {} state: {}", Thread.currentThread().getName(), Thread.currentThread().getState());
            stop = sleepAndStop();
            //Thread.onSpinWait(); // "новая фича"
          }
        });
    thread.setName("Named-thread");
    thread.setDaemon(false);
    log.info("state: {}", thread.getState());

    thread.start();

    sleep();
    log.info("interrupting");
    thread.interrupt();

    thread.join();

    log.info("finished");
  }

  private static boolean sleepAndStop() {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(1));
      return false;
    } catch (InterruptedException e) {
      log.info("somebody is trying to stop us, Ok");
      Thread.currentThread().interrupt();
      return true;
    }
  }

  private static void sleep() {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(10));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

  }
}
