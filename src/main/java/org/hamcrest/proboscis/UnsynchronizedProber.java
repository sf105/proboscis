package org.hamcrest.proboscis;


/**
 * The default Prober. Just applies a probe without any further synchronization.
 */
public class UnsynchronizedProber extends PollingProber {
    public UnsynchronizedProber(long timeoutMillis, long pollDelayMillis) {
        super(timeoutMillis, pollDelayMillis);
    }

    @Override
    protected void runProbe(Probe probe) {
        probe.probe();
    }
}
