package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;

public class Order {
    private static int idCounter;
    private final int id;
    private OrderState state;
    private String assignedPrinterId;
    private int assignmentCount;
    private int validationCount;
    private int printingCount;
    private int qualityControlCount;

    public Order() {
        this.id = generateIdThreadSafe();
        this.state = OrderState.CREATED;
        this.assignedPrinterId = null;
        this.assignmentCount = 0;
        this.validationCount = 0;
        this.printingCount = 0;
        this.qualityControlCount = 0;
    }

    private static synchronized int generateIdThreadSafe() {
        idCounter++;
        return idCounter;
    }

    public void assignPrinter(String a) {
        this.assignedPrinterId = a;
    }

    public void send2ValidateOrder() {
        this.state = OrderState.WAITING_VALIDATION;
    }

    public void send2PrintOrder() {
        this.state = OrderState.READY_TO_PRINT;
    }

    public void rejectOrder() {
        this.state = OrderState.REJECTED;
    }

    public void printOrder() {
        this.state = OrderState.PRINTED;
    }

    public void failPrintingOrder() {
        this.state = OrderState.PRINT_FAILED;
    }

    public void approveOrder() {
        this.state = OrderState.APPROVED;
    }

    public void defectOrder() {
        this.state = OrderState.DEFECTIVE;
    }

    public void incrementCount(int code) {
        switch (code) {
            case 0: ++assignmentCount;
            case 1: ++validationCount;
            case 2: ++printingCount;
            case 3: ++qualityControlCount;
        }
    }

    public int getId() {
        return id;
    }

    public OrderState getState() {
        return state;
    }

    public int getAssignmentCount() {
        return assignmentCount;
    }

    public int getValidationCount() {
        return validationCount;
    }

    public int getPrintingCount() {
        return printingCount;
    }

    public int getQualityControlCount() {
        return qualityControlCount;
    }

    public String getAssignedPrinterId() {
        return assignedPrinterId;
    }
}
