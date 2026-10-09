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

        writer.write("Secuencia; TiempoTranscurrido[ms]; NombreHilo; OrderID; Etapa; Evento; deEstado; aEstado; Impresora");
        writer.newLine();
        writer.flush();
    }

    public synchronized void logEvent(int orderID, int stageCode, int eventCode, OrderState from, OrderState to, String printerID) {
        try {
            long elapsedMS = Math.max(0, System.currentTimeMillis() - startTimeMS);
            String threadName = Thread.currentThread().getName();

            String fromStr = (from != null) ? from.name() : "";
            String printerStr = (printerID != null) ? printerID : "";

            String line = String.format("%d;%d;%s;%d;%s;%s;%s;%s;%s", sequence++, elapsedMS, threadName, orderID, getStageFromCode(stageCode), getEventFromCode(eventCode), fromStr, to.name(), printerStr);

            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error no se pudo escribir el archova!!1", e);
        }
    }

    public String getStageFromCode(int a) {
        return switch (a) {
            case 0 -> "INITIALIZATION";
            case 1 -> "ASSIGNMENT";
            case 2 -> "VALIDATION";
            case 3 -> "PRINTING";
            case 4 -> "QUALITY_CONTROL";
            default -> "Error";
        };
    }

    public String getEventFromCode(int a) {
        return switch (a) {
            case 0 -> "ORDER_CREATED";
            case 1 -> "ORDER_STATE_CHANGED";
            default -> "Error";
        };
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
