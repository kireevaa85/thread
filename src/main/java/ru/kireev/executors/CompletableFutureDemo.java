package ru.kireev.executors;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CompletableFutureDemo {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    new CompletableFutureDemo().go();
  }

  private static void sleep() {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(3));
    } catch (InterruptedException e) {
      log.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  private void go() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> task(1));
    log.info("thread is not blocked");
    log.info("result1:{}", future1.get());

    CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> task(2));
    future2.thenAccept(val -> log.info("result2:{}", val));
    future2.join();

    CompletableFuture<String> future3 = CompletableFuture.supplyAsync(this::errorAction);
    future3.exceptionally(Throwable::getMessage).thenAccept(msg -> log.info("msg:{}", msg));

    CompletableFuture<String> futureT1 = CompletableFuture.supplyAsync(() -> task(100));
    CompletableFuture<String> futureT2 = CompletableFuture.supplyAsync(() -> task(200));
    CompletableFuture<Void> joinedResult = futureT1.thenAcceptBoth(futureT2, (s1, s2) -> log.info("joinedResult: {}, {}", s1, s2));
    joinedResult.join();

//        CompletableFuture<String> futureT1 = CompletableFuture.supplyAsync(() -> task(100));
//        CompletableFuture<String> futureT2 = CompletableFuture.supplyAsync(() -> task(200));
//        CompletableFuture<Void> firstResult = futureT1.acceptEither(futureT2, s -> log.info("firstResult: {}", s));
//        firstResult.join();
  }

  private String errorAction() {
    throw new TestException("error for Test");
  }

  @NotNull
  private String task(int id) {
    sleep();
    log.info("task is done: {}", id);
    return "done" + id;
  }

  private static class TestException extends RuntimeException {
    TestException(String msg) {
      super(msg);
    }
  }
}
