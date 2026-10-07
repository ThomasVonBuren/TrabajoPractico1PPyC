package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.OrderState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Logger implements AutoCloseable{
    private final BufferedWriter writer;
    private final long startTimeMS;
    private long sequence = 1;

    public Logger(Path output, long startTimeMS) throws IOException {
        this.startTimeMS = startTimeMS;
        Path filePath = output.resolve("eventos.csv");
        this.writer = new BufferedWriter(new FileWriter(filePath.toFile()));

        writer.write("Secuencia; TiempoTranscurrido[ms]; OrderID; Etapa; Evento; deEstado; aEstado; Impresora");
        writer.newLine();
        writer.flush();
    }

    public synchronized void logEvent(int orderID, String stage, String event, OrderState from, OrderState to, String printerID) {
        try {
            long elapsedMS = Math.max(0, System.currentTimeMillis() - startTimeMS);
            String threadName = Thread.currentThread().getName();

            String fromStr = (from != null) ? from.name() : "";
            String printerStr = (printerID != null) ? printerID : "";

            String line = String.format("%d;%d;%s;%d;%s;%s;%s;%s;%s", sequence++, elapsedMS, threadName, orderID, stage, event, fromStr, to.name(), printerStr);

            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error no se pudo escribir el archova!!1", e);
        }
    }

    @Override
    public void close() {
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Errro al cerrar el archova!!1", e);
        }

    }
}
