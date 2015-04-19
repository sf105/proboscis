package test.hamcrest.proboscis;

import org.hamcrest.Description;
import org.hamcrest.proboscis.Probe;
import org.hamcrest.proboscis.Prober;
import org.hamcrest.proboscis.UnsynchronizedProber;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class UnsynchronizedProberTest {
    static final int TIMEOUT = 250;
    public static final int LIKELY_NUMBER_OF_PROBES = 3;
    @Rule public final ExpectedException thrown= ExpectedException.none();
    final Prober prober = new UnsynchronizedProber(TIMEOUT, 50);
    
    @Test public void
    assertionFailsIfProbeIsNotSatisfiedWithinTimeout() {
        thrown.expectMessage("an error description");
        new UnsynchronizedProber(1, 1).check(
            new Probe() {
                public void probe() { }
                public boolean isSatisfied() {
                    return false;
                }
                public void describeTo(Description description) {
                    description.appendText("an error description");
                }
                public void describeFailureTo(Description description) { }
            });
    }
    
    @Test public void
    assertionPassesIfProbeIsSatisfied() {
        new UnsynchronizedProber(1, 1).check(
          new Probe() {
              public void probe() {}
              public boolean isSatisfied() {
                return true;
            }
              public void describeTo(Description description) { }
              public void describeFailureTo(Description description) { }
          });
    }

    @Test public void
    repeatedlyPollsProbeUntilItIsSatisfied() {
        new UnsynchronizedProber(10, 1).check(
          new Probe() {
              int probeCount = 0;
              public void probe() { probeCount++; }
              public boolean isSatisfied() { return probeCount > LIKELY_NUMBER_OF_PROBES; }
              public void describeTo(Description description) { }
              public void describeFailureTo(Description description) { }
          });
    }
    
    @Test public void
    runsProbeInTestThread() {
        final Thread testThread = Thread.currentThread();
        
        prober.check(new Probe() {
            boolean wasOnTestThread = false;
            
            public void probe() {
                wasOnTestThread = Thread.currentThread() == testThread;
            }
            
            public boolean isSatisfied() {
                return wasOnTestThread;
            }

            public void describeFailureTo(Description description) {
                description.appendText("was not test thread");
            }

            public void describeTo(Description description) {
                description.appendText("run on test thread");
            }
        });
    }
}
