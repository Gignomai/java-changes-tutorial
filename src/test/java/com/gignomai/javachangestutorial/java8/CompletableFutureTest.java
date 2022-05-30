package com.gignomai.javachangestutorial.java8;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompletableFutureTest {

    @Test
    void shouldCreateAndCompleteACompletableFuture() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = new CompletableFuture<>();

        completableFuture.complete("Hello world");

        assertThat(completableFuture.get()).isEqualTo("Hello world");
    }

    @Test
    void shouldCreateACompletedCompletableFuture() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("Hello world");

        assertThat(completableFuture.get()).isEqualTo("Hello world");
    }

    @Test
    void shouldRunAsyncMethod() throws ExecutionException, InterruptedException {
        final CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(this::blockingVoidMethod);

        completableFuture.get();

        assertThat(completableFuture.isDone()).isTrue();
    }

    @Test
    void shouldSupplyAsyncMethod() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod);

        assertThat(completableFuture.get()).isEqualTo("Hello");
    }

    @Test
    void shouldChainSupplyAsyncAndThenAccept() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod);

        final CompletableFuture<Void> resultCompletableFuture = completableFuture
                .thenAccept(s -> blockingVoidMethodWithParameter(s + " World!"));

        resultCompletableFuture.get();

        assertThat(resultCompletableFuture.isDone()).isTrue();
    }

    @Test
    void shouldChainSupplyAsyncAndThenApply() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod);

        final CompletableFuture<String> resultCompletableFuture = completableFuture
                .thenApply(this::blockingStringMethodWithParameter);

        assertThat(resultCompletableFuture.get()).isEqualTo("Hello world!");
    }

    @Test
    void shouldChainSupplyAsyncAndThenApplyShort() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod)
                .thenApply(this::blockingStringMethodWithParameter);

        assertThat(completableFuture.get()).isEqualTo("Hello world!");
    }

    @Test
    void shouldChainMoreThanOneCompletionStages() throws ExecutionException, InterruptedException {
        final CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod)
                .thenApply(this::blockingStringMethodWithParameter)
                .thenAccept(this::blockingVoidMethodWithParameter)
                .thenRun(() -> System.out.println("Finished!"));

        completableFuture.get();

        assertThat(completableFuture.isDone()).isTrue();
    }

    @Test
    void shouldChainMoreThanOneCompletionStagesWithAsyncMethods() throws ExecutionException, InterruptedException {
        final CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod)
                .thenApplyAsync(this::blockingStringMethodWithParameter)
                .thenAcceptAsync(this::blockingVoidMethodWithParameter)
                .thenRunAsync(() -> System.out.println("Finished!"));

        completableFuture.get();

        assertThat(completableFuture.isDone()).isTrue();
    }

    @Test
    void shouldComposeFutures() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::blockingStringMethod)
                .thenCompose(this::stringCompletableFutureMethodWithParam);

        assertThat(completableFuture.get()).isEqualTo("Hello World");
    }

    @Test
    void shouldCombineFutures() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(this::blockingStringMethod)
                .thenCombine(stringCompletableMethodFuture(), concatStringsBiFunction());

        assertThat(completableFuture.get()).isEqualTo("Hello World");
    }

    @Test
    void shouldRunMultipleFuturesInParallel() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        final CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
        final CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");

        final CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);

        combinedFuture.get();

        assertThat(future1.isDone()).isTrue();
        assertThat(future2.isDone()).isTrue();
        assertThat(future3.isDone()).isTrue();

        final String combined = Stream.of(future1, future2, future3)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));

        assertThat(combined).isEqualTo("Hello Beautiful World");
    }

    @Test
    void shouldHandleExceptionInFuture() throws ExecutionException, InterruptedException {
        final String name = getNullString();

        final CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(() -> processNullableString(name))
                .handle((s, t) -> s != null ? s : "Hello, Stranger!");

        assertThat(completableFuture.get()).isEqualTo("Hello, Stranger!");
    }

    @Test
    void shouldCompleteFutureExceptionally() {
        final CompletableFuture<String> completableFuture = new CompletableFuture<>();

        completableFuture.completeExceptionally(
                new CustomRuntimeException("Calculation failed!"));

        assertThrows(ExecutionException.class, completableFuture::get);
    }

    @Test
    void shouldManageExceptionsInAChainOfFutures() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::getNullString)
                .thenApply(this::processNullableString)
                .exceptionally((error)-> "Error: " + error.getLocalizedMessage());

        assertThat(completableFuture.get()).startsWith("Error: ");
    }

    private BiFunction<String, String, String> concatStringsBiFunction() {
        return (firstFutureResult, secondFutureResult) -> firstFutureResult + secondFutureResult;
    }

    private void blockingVoidMethod() {
        try {
            Thread.sleep(500);
            System.out.println("Method blockingVoidMethod running in thread: " + Thread.currentThread().getName());
            System.out.println("Hello World!");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getLocalizedMessage());
        }
    }

    private void blockingVoidMethodWithParameter(String message) {
        try {
            Thread.sleep(500);
            System.out.println("Method blockingVoidMethodWithParameter running in thread: "
                    + Thread.currentThread().getName());
            System.out.println(message);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getLocalizedMessage());
        }
    }

    private String blockingStringMethod() {
        try {
            Thread.sleep(500);
            System.out.println("Method blockingStringMethod running in thread: " + Thread.currentThread().getName());
            return "Hello";
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private String blockingStringMethodWithParameter(String message) {
        try {
            Thread.sleep(500);
            System.out.println("Method blockingStringMethodWithParameter running in thread: "
                    + Thread.currentThread().getName());
            return message + " world!";
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private CompletableFuture<String> stringCompletableMethodFuture() {
        return CompletableFuture.supplyAsync(() -> " World");
    }

    private CompletableFuture<String> stringCompletableFutureMethodWithParam(String message) {
        return CompletableFuture.supplyAsync(() -> message + " World");
    }

    private static class CustomRuntimeException extends Throwable {
        public CustomRuntimeException(String message) {
            System.out.println(message);
        }
    }

    private String processNullableString(String name) {
        if (name == null) {
            throw new RuntimeException("Computation error!");
        }
        return "Hello, " + name;
    }

    private String getNullString() {
        return null;
    }
}
