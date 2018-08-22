package fj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class FjTest1 extends RecursiveTask<String> {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        final String result = ForkJoinPool.commonPool().invoke(new FjTest1());
        System.out.println(result);
    }

    @Override
    protected String compute() {
        final SleepTask task1 = new SleepTask("task1");
        final SleepTask task2 = new SleepTask("task2");
        invokeAll(task1, task2);

//        task1.fork();
//        task2.fork();
        System.out.println("test");

        IntStream.rangeClosed(1, 5)
                .forEach(i -> {
                    final String join2 = task2.join();
                    System.out.println(join2);

                    final String join1 = task1.join();
                    System.out.println(join1);
                });

        return getClass().getName();
    }
}
