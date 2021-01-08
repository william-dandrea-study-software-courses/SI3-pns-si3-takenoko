package fr.matelots.polytech.engine.util;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Gabriel Cogne
 */
public class LogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        String builder = record.getLevel() +
                " : " +
                record.getMessage();
        System.out.println(builder);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

}
