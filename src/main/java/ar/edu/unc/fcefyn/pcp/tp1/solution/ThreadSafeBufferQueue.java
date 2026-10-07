package ar.edu.unc.fcefyn.pcp.tp1.solution;

import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadSafeBufferQueue<T> {
    private final Queue<T> threads = new ArrayDeque<>();
    private boolean open = true;

    public synchronized void push(T thr) {
        threads.add(thr);
        notifyAll();
    }

    public synchronized T pop() throws InterruptedException {
        while (threads.isEmpty() && open) {
            wait();
        }
        if (threads.isEmpty() && !open) {
            return null;
        }
        return threads.poll();
    }
    public synchronized void close() {
        this.open = false;
        notifyAll();
    }
}
