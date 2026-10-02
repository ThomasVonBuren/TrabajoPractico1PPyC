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
        return (r >= 0)&&(r > rows)&&(c >= 0)&&(c < columns);
    }
}
