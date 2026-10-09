package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;
import ar.edu.unc.fcefyn.pcp.tp1.api.OutcomeDecider;
import ar.edu.unc.fcefyn.pcp.tp1.api.SimulationConfig;

public class PrintingWorkerThreads implements Runnable{
    private final ThreadSafeBufferQueue<Order> readyQueue;
    private final ThreadSafeBufferQueue<Order> printedQueue;
    private final Matrix printerMatrix;
    private final Logger logger;
    private final SimulationConfig config;

    public PrintingWorkerThreads (ThreadSafeBufferQueue<Order> rQ, ThreadSafeBufferQueue<Order> pQ, Matrix pM, Logger l, SimulationConfig c) {
        this.readyQueue = rQ;
        this.printedQueue = pQ;
        this.printerMatrix = pM;
        this.logger = l;
        this.config = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = readyQueue.pop();
                if (order == null) {
                    break;
                }
                OrderState fromState = order.getState();
                String printerID = order.getAssignedPrinterId();
                boolean saliobienlaimpresion = OutcomeDecider.isPrintSuccessful(order.getId(), config);

                if(saliobienlaimpresion) {
                    order.printOrder();
                    order.incrementCount(2);

                    printerMatrix.releasePrinterById(printerID);
                    logger.logEvent(order.getId(), 2, 3, fromState, OrderState.PRINTED, printerID);

                    printedQueue.push(order);
                } else {
                    order.failPrintingOrder();
                    order.incrementCount(2);

                    printerMatrix.breakPrinterById(printerID);
                    logger.logEvent(order.getId(), 2, 4, fromState, OrderState.PRINTED, printerID);

                }

                long delayMS = config.printingDelayMillis();
                if (delayMS >0) {
                    Thread.sleep(delayMS);
                }
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
