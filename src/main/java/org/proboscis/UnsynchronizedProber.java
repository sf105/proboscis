package org.proboscis;


public class UnsynchronizedProber extends PollingProber {
    public UnsynchronizedProber(long timeoutMillis, long pollDelayMillis) {
        super(timeoutMillis, pollDelayMillis);
    }

    @Override
    protected void runProbe(Probe probe) {
        probe.probe();
    }
}
