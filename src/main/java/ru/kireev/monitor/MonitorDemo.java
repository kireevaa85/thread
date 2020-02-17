package ru.kireev.monitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonitorDemo {
  private static final int LIMIT = 100_000_000;
  private final Object monitor = new Object();
  private int count = 0;

  public static void main(String[] args) throws InterruptedException {
    var counter = new MonitorDemo();
    counter.go();
  }

  private static void inc1(MonitorDemo demo) {
    synchronized (MonitorDemo.class) {
      for (int i = 0; i < LIMIT; i++)
        demo.count++;
    }
  }

  //ошибочное импользование мониторов - у каждого потока свой монитор.
  private void inc2() {
    synchronized (monitor) {
      for (int i = 0; i < LIMIT; i++) {
        count++;
      }
    }
  }

  private void inc3() {
    synchronized (this) {
      for (int i = 0; i < LIMIT; i++)
        count++;
    }
  }

  private void go() throws InterruptedException {
    Thread thread1 = new Thread(() -> inc1(this));
//    Thread thread2 = new Thread(() -> inc1(this));
//    Thread thread3 = new Thread(() -> inc1(this));
    Thread thread2 = new Thread(this::inc2);
    Thread thread3 = new Thread(this::inc3);

    thread1.start();
    thread2.start();
    thread3.start();

    thread1.join();
    thread2.join();
    thread3.join();
    log.info("CounterBroken: {}", count);
  }
}
