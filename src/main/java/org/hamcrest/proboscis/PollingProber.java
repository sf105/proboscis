package org.hamcrest.proboscis;

import org.hamcrest.StringDescription;

public abstract class PollingProber implements Prober {
    private long timeoutMillis;
    private long pollDelayMillis;

    public PollingProber(long timeoutMillis, long pollDelayMillis) {
        this.timeoutMillis = timeoutMillis;
        this.pollDelayMillis = pollDelayMillis;
    }

    public void check(Probe probe) {
        if (!poll(probe)) {
            throw new AssertionError(failureDescriptionOf(probe));
        }
    }

    protected abstract void runProbe(Probe probe);

    protected String failureDescriptionOf(Probe probe) {
        final StringDescription description = new StringDescription();
   
        description.appendText("\nTried to find:\n    ")
            .appendDescriptionOf(probe)
            .appendText("\nbut:\n    ");
        probe.describeFailureTo(description);

        return description.toString();
    }

    private boolean poll(Probe probe) {
        final Timeout timeout = new Timeout(timeoutMillis);

        while (true) {
            runProbe(probe);
            
            if (probe.isSatisfied()) {
                return true;
            }
            else if (timeout.hasTimedOut()) {
                return false;
            }
            else {
                waitFor(pollDelayMillis);
            }
        }
    }
    
    private void waitFor(long duration) {
        try {
            Thread.sleep(duration);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("unexpected interrupt", e);
        }
    }
}
