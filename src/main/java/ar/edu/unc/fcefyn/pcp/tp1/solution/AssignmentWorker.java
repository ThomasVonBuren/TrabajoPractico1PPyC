package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;

public class AssignmentWorker implements Runnable {
    private final ThreadSafeBufferQueue<Order> inQueue;
    private final ThreadSafeBufferQueue<Order> outQueue;
    private final Matrix matrix;
    private final long assignmentDelayMs;
    private final Logger logger;

    public AssignmentWorker(ThreadSafeBufferQueue<Order> inQueue, ThreadSafeBufferQueue<Order> outQueue,Matrix matrix, long assignmentDelayMs, Logger logger) {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.matrix = matrix;
        this.assignmentDelayMs = assignmentDelayMs;
        this.logger = logger;
    }
    @Override
    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted()){
                Order order = inQueue.pop();
                if (order == null){
                    break;
                }
                Printer printer = matrix.getAvailableAndReserve(order);
                order.assignPrinter(printer.getId());
                order.send2ValidateOrder();
                order.incrementCount(0);

                if (assignmentDelayMs > 0){
                    Thread.sleep(assignmentDelayMs);
                }
                logger.logEvent(order.getId(), 0, 1, OrderState.CREATED, OrderState.WAITING_VALIDATION, printer.getId());
                outQueue.push(order);
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

}
