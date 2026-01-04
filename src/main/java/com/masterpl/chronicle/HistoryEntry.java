package com.masterpl.chronicle;

public class HistoryEntry {
    private long timestamp;
    private String key;
    private String[] args;

    public HistoryEntry(long timestamp, String key, String[] args) {
        this.timestamp = timestamp;
        this.key = key;
        this.args = args;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getKey() {
        return key;
    }

    public String[] getArgs() {
        return args;
    }
}
