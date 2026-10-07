package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;
import ar.edu.unc.fcefyn.pcp.tp1.api.PrinterState;
import ar.edu.unc.fcefyn.pcp.tp1.api.SimulationConfig;

public class Printer {
    private static int idCounter;
    private final String id;
    private static int rowGlobal = 0;
    private static int columnGlobal = 0;
    private final Pos position;
    private PrinterState currentState;
    private int usageCount;
    private Order currentOrder;

    public Printer(SimulationConfig config){
        this.id = generateIdThreadSafe();
        int maxCol = config.printerColumns();
        this.position = getNextPos(maxCol);
        this.currentState = PrinterState.AVAILABLE;
        this.usageCount = 0;
        this.currentOrder = null;
    }

    private static synchronized String generateIdThreadSafe() {
        idCounter++;
        return String.valueOf(idCounter);
    }

    private static synchronized Pos getNextPos(int maxC) {
        Pos actual = new Pos(rowGlobal, columnGlobal);

        columnGlobal++;
        if (columnGlobal >= maxC) {
            columnGlobal = 0;
            rowGlobal++;
        }

        return actual;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public Pos getPosition() {
        return position;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public PrinterState getCurrentState() {
        return currentState;
    }

    public String getId() {
        return id;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
        currentOrder.assignPrinter(this.id);
    }

    public void setNoOrder() {
        this.currentOrder = null;
    }

    public void setCurrentState(PrinterState currentState) {
        this.currentState = currentState;
    }

    //Las siguientes no se deberían usar
    public static void setIdCounter(int idCounter) {
        Printer.idCounter = idCounter;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public void usePrinter() { ++this.usageCount; }
}
