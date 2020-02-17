package ru.kireev.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockDemo {

  private final Lock lock = new ReentrantLock();

  public static void main(String[] args) {
    new ReentrantLockDemo().go();
  }

  private static void sleep() {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(10));
    } catch (InterruptedException e) {
      log.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  private void go() {
    Thread t1 = new Thread(this::criticalSection);
    t1.setName("t1");
    t1.start();

    Thread t2 = new Thread(this::criticalSection);
    t2.setName("t2");
    t2.start();
  }

  private void criticalSection() {
    log.info("before critical section");
    lock.lock();
    try {
      log.info("in the critical section");
      sleep();
    } finally {
      lock.unlock();
    }
    log.info("after critical section");
  }
}
