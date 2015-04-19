package org.hamcrest.proboscis;

import org.hamcrest.StringDescription;

public abstract class PollingProber implements Prober {
    private long timeoutMillis;
    private long pollDelayMillis;

    public PollingProber(long timeoutMillis, long pollDelayMillis) {
        this.timeoutMillis = timeoutMillis;
        this.pollDelayMillis = pollDelayMillis;
    }

    public long getTimeout() {
        return timeoutMillis;
    }

    public void setTimeout(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public long getPollDelay() {
        return pollDelayMillis;
    }

    public void setPollDelay(long pollDelayMillis) {
        this.pollDelayMillis = pollDelayMillis;
    }

    public void check(Probe probe) {
        if (!poll(probe)) {
            throw new AssertionError(describeFailureOf(probe));
        }
    }
    
    protected String describeFailureOf(Probe probe) {
        StringDescription description = new StringDescription();
   
        description.appendText("\nTried to find:\n    ");
        probe.describeTo(description);
        description.appendText("\nbut:\n    ");
        probe.describeFailureTo(description);

        return description.toString();
    }

    private boolean poll(Probe probe) {
        Timeout timeout = new Timeout(timeoutMillis);
    
        for (;;) {
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
    
    protected abstract void runProbe(Probe probe);

    private void waitFor(long duration) {
        try {
            Thread.sleep(duration);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("unexpected interrupt", e);
        }
    }
}
