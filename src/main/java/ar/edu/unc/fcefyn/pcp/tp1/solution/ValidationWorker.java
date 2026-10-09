package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;
import ar.edu.unc.fcefyn.pcp.tp1.api.OutcomeDecider;
import ar.edu.unc.fcefyn.pcp.tp1.api.SimulationConfig;

import java.util.concurrent.TimeUnit;

public class ValidationWorker implements Runnable {
    private final ThreadSafeBufferQueue<Order> inQueue;
    private final ThreadSafeBufferQueue<Order> outQueue;
    private final Matrix matrix;
    private final long validationDelayMs;
    private final Logger logger;

    private final SimulationConfig config;

public ValidationWorker(ThreadSafeBufferQueue<Order> inQueue, ThreadSafeBufferQueue<Order> outQueue, Matrix matrix, long validationDelayMs, Logger logger,  SimulationConfig config) {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.matrix = matrix;
        this.validationDelayMs = validationDelayMs;
        this.logger = logger;

        this.config = config;
    }

    @Override
    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted()){
                Order order = inQueue.pop();
                if(order == null){
                    break;
                }
                if (validationDelayMs > 0){
                    Thread.sleep(validationDelayMs);
                }
                if(outcomeDecider.isModelValid(order.getId(), config)){
                    order.send2PrintOrder();
                    order.incrementCount(1);
                    logger.logEvent(order.getId(), 2, 1, OrderState.WAITING_VALIDATION, OrderState.READY_TO_PRINT, order.getAssignedPrinterId());
                    outQueue.push(order);
                } else{
                    order.rejectOrder();
                    order.incrementCount(1);
                    //Printer printer = matrix.getPrinter(order.getAssignedPrinterId());
                    String printerId = order.getAssignedPrinterId();
                    matrix.releasePrinterById(printerId);
                    logger.logEvent(order.getId(), 2, 1, OrderState.WAITING_VALIDATION, OrderState.REJECTED, order.getAssignedPrinterId());
                }
            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
