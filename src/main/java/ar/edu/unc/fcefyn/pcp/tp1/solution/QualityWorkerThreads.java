package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;
import ar.edu.unc.fcefyn.pcp.tp1.api.OutcomeDecider;
import ar.edu.unc.fcefyn.pcp.tp1.api.SimulationConfig;

public class QualityWorkerThreads implements Runnable{
    private final ThreadSafeBufferQueue<Order> printedQueue;
    private final Logger logger;
    private final SimulationConfig config;

    public QualityWorkerThreads(ThreadSafeBufferQueue<Order> pQ, Logger l, SimulationConfig c) {
        this.printedQueue = pQ;
        this.logger = l;
        this.config = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Order order = printedQueue.pop();
                if (order == null) {
                    break;
                }
                OrderState fromState = order.getState();
                String printerID = order.getAssignedPrinterId();

                boolean impresionaprobada = OutcomeDecider.isQualityApproved(order.getId(), config);

                if (impresionaprobada) {
                    order.approveOrder();
                    order.incrementCount(3);

                    logger.logEvent(order.getId(), 4, 1, fromState, OrderState.APPROVED, printerID);
                } else {
                    order.defectOrder();
                    order.incrementCount(3);

                    logger.logEvent(order.getId(), 4, 1, fromState, OrderState.DEFECTIVE, printerID);
                }
                    long delayMS = config.qualityControlDelayMillis();
                    if (delayMS > 0) {
                        Thread.sleep(delayMS);
                    }
                }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
