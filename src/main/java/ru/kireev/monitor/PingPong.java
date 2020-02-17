package ru.kireev.monitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingPong {
  private String last = "PONG";

  public static void main(String... __) {
    var pingPong = new PingPong();
    new Thread(() -> pingPong.action("ping")).start();
    new Thread(() -> pingPong.action("PONG")).start();
  }

  private static void sleep() {
    try {
      Thread.sleep(1_000);
    } catch (InterruptedException e) {
      log.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  private synchronized void action(String message) {
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      try {
        //spurious wakeup
        while (last.equals(message))
          wait();

        log.info(message);
        last = message;
        sleep();
        notifyAll();

      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        throw new NotInterestingException(ex);
      }
    }
  }

  private class NotInterestingException extends RuntimeException {
    NotInterestingException(InterruptedException ex) {
      super(ex);
    }
  }
}
