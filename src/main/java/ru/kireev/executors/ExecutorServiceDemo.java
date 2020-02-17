package ru.kireev.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ExecutorServiceDemo {
  private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceDemo.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {
//    new ExecutorServiceDemo().singleThread();
//    new ExecutorServiceDemo().newFixedThreadPool();
    new ExecutorServiceDemo().scheduledThreadPoolExecutor();
  }

  private static void sleep() {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(3));
    } catch (InterruptedException e) {
      logger.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  private String task(int id) {
    sleep();
    logger.info("call is done:{}", id);
    return "done " + id;
  }

  private void singleThread() throws ExecutionException, InterruptedException {
    //Один поток выполняет задачи из внутренней НЕОГРАНИЧЕННОЙ очереди
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<String> resultInFuture1 = executor.submit(() -> task(1));
    logger.info("task1 submitted");

    Future<String> resultInFuture2 = executor.submit(() -> task(2));
    logger.info("task2 submitted");

    Future<String> resultInFuture3 = executor.submit(() -> task(3));
    logger.info("task3 submitted");

    String result1 = resultInFuture1.get();
    String result2 = resultInFuture2.get();
    String result3 = resultInFuture3.get();

    logger.info("result1:{}", result1);
    logger.info("result2:{}", result2);
    logger.info("result3:{}", result3);

    executor.shutdown();
  }

  private void newFixedThreadPool() throws ExecutionException, InterruptedException {
    //Заданное количество потоков выполняют задачи из внутренней НЕОГРАНИЧЕННОЙ очереди
//    ExecutorService executor = Executors.newFixedThreadPool(3);
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    Future<String> resultInFuture1 = executor.submit(() -> task(1));
    logger.info("task1 submitted");

    Future<String> resultInFuture2 = executor.submit(() -> task(2));
    logger.info("task2 submitted");

    Future<String> resultInFuture3 = executor.submit(() -> task(3));
    logger.info("task3 submitted");

    String result1;
    //    if (resultInFuture1.isDone())
    result1 = resultInFuture1.get();

    String result2 = resultInFuture2.get();
    String result3 = resultInFuture3.get();

    logger.info("result1:{}", result1);
    logger.info("result2:{}", result2);
    logger.info("result3:{}", result3);

    executor.shutdown();
  }

  private void scheduledThreadPoolExecutor() {
    //Заданное количество потоков выполняют задачи с задержкой или периодически
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(() -> logger.info("task is done"), 0, 3, TimeUnit.SECONDS);
  }
}
