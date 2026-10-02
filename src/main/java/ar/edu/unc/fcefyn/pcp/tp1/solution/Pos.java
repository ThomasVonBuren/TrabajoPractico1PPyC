package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.SimulationConfig;

public class Pos {
    private int n; // columnas
    private int m; // filas

    public Pos(int a, int b) { // fil , col
        this.n = a;
        this.m = b;
    }

    @Override
    public String toString() {
        return "(" + n + ";" + n + ")";
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }
}
