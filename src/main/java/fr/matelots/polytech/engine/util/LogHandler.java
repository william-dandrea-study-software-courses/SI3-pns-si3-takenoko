package fr.matelots.polytech.engine.util;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {
    @Override
    public void publish(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(record.getLevel());
        builder.append(" : ");
        builder.append(record.getMessage());

        System.out.println(builder.toString());
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
