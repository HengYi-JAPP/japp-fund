package fj;

import java.util.concurrent.RecursiveTask;

public class SleepTask extends RecursiveTask<String> {
    private final String s;

    public SleepTask(String s) {
        this.s = s;
    }

    @Override
    protected String compute() {
        System.out.println(getClass().getName() + "[" + s + "]:compute");
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return s;
    }
}
