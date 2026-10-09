package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.PrinterState;
import ar.edu.unc.fcefyn.pcp.tp1.api.SimulationConfig;

import java.util.NoSuchElementException;

public class Matrix {
    private Printer[][] M;
    private final int rows;
    private final int columns;

    public Matrix(SimulationConfig config){
        this.rows = config.printerRows();
        this.columns = config.printerColumns();
        this.M = new Printer[rows][columns];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {

            }
        }
    }

    public Matrix(int r, int c){
        this.rows = r;
        this.columns = c;
        this.M = new Printer[r][c];
    }

    public void addPrinters(Printer P) throws IndexOutOfBoundsException {
        Pos p = P.getPosition();
        int r = p.getM();
        int c = p.getN();

        if (isValidPos(r,c)){
            this.M[r][c] = P;
        } else {
            throw new IndexOutOfBoundsException("Posición fuera de límites de Matriz");
        }
    }

    public Printer getPrinter(int r, int c){
        if (isValidPos(r,c)){
            Printer a = M[r][c];
            if(a == null){
                throw new NoSuchElementException("La impresora solicitada no existe");
            }
            return a;
        } else {
            throw new IndexOutOfBoundsException("Posición fuera de límites de Matriz");
        }

    }

    public Printer getPrinter(Pos p){
        return M[p.getM()][p.getN()];
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    private boolean isValidPos(int r, int c){
        return (r >= 0)&&(r < rows)&&(c >= 0)&&(c < columns);
    }

    public synchronized  Printer getAvailableAndReserve(Order o) throws InterruptedException {
        while (true) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    Printer p = M[r][c];

                    if (p.getCurrentState() == PrinterState.AVAILABLE) {
                        p.setCurrentState(PrinterState.RESERVED);
                        p.usePrinter();
                        p.setCurrentOrder(o);

                        return p;
                    }
                }
            }
            wait();
        }
    }

    public synchronized void releasePrinter(Printer p) {
        p.setCurrentState(PrinterState.AVAILABLE);
        p.setNoOrder();
        notifyAll();
    }

    public synchronized void releasePrinterById(String printerId) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (M[r][c] != null && M[r][c].getId().equals(printerId)) {
                    releasePrinter(M[r][c]);
                    return;
                }
            }
        }
    }

    public synchronized void breakPrinter(Printer p) {
        p.setCurrentState(PrinterState.OUT_OF_SERVICE);
        p.setNoOrder();
    }

    public synchronized void breakPrinterById(String printerId) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (M[r][c] != null && M[r][c].getId().equals(printerId)) {
                    breakPrinter(M[r][c]);
                    return;
                }
            }
        }
    }
}

