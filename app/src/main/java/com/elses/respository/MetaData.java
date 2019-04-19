package com.elses.respository;

import java.util.Date;

public class MetaData {
    public String getRecordingStartedAt() {
        return recordingStartedAt;
    }

    public void setRecordingStartedAt(String recordingStartedAt) {
        this.recordingStartedAt = recordingStartedAt;
    }

    public String recordingStartedAt;
    public String recordingStoppedAt;

    public String getRecordingStoppedAt() {
        return recordingStoppedAt;
    }

    public void setRecordingStoppedAt(String recordingStoppedAt) {
        this.recordingStoppedAt = recordingStoppedAt;
    }

    public int getReccordingIntervalMinutes() {
        return reccordingIntervalMinutes;
    }

    public void setReccordingIntervalMinutes(int reccordingIntervalMinutes) {
        this.reccordingIntervalMinutes = reccordingIntervalMinutes;
    }

    public int getNumberOfRecordings() {
        return numberOfRecordings;
    }

    public void setNumberOfRecordings(int numberOfRecordings) {
        this.numberOfRecordings = numberOfRecordings;
    }

    public int reccordingIntervalMinutes;
    public int numberOfRecordings;

}
