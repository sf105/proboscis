package test.hamcrest.proboscis;

import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.proboscis.Probe;
import org.hamcrest.proboscis.Prober;
import org.hamcrest.proboscis.UnsynchronizedProber;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;


public class UnsynchronizedProberTest {
    static final int TIMEOUT = 250;
    
    Prober prober = new UnsynchronizedProber(TIMEOUT, 50);
    
    @Test public void
    assertionFailsIfProbeIsSatisfiedWithinTimeout() {
        try {
            prober.check(new Probe() {
                public void probe() {
                }
                
                public boolean isSatisfied() {
                    return false;
                }
                
                public void describeTo(Description description) {
                    description.appendText("error message");
                }

                public void describeFailureTo(Description description) {
                }
            });
        }
        catch (AssertionError e) {
            MatcherAssert.assertThat(e.getMessage(), containsString("error message"));
        }
    }
    
    @Test public void
    assertionPassesIfProbeIsSatisfied() {
        prober.check(new Probe() {
            public void probe() {
            }
            
            public boolean isSatisfied() {
                return true;
            }
            
            public void describeTo(Description description) {
            }

            public void describeFailureTo(Description description) {
            }
        });
    }

    @Test public void
    repeatedlyPollsProbeUntilItIsSatisfied() {
        final long start = System.currentTimeMillis();
        
        prober.check(new Probe() {
            long probeTime;
            
            public void probe() {
                probeTime = System.currentTimeMillis();
            }
            
            public boolean isSatisfied() {
                return probeTime-start > 100 ;
            }
            
            public void describeTo(Description description) {
            }

            public void describeFailureTo(Description description) {
            }
        });
    }
    
    @Test public void
    runsProbeOnTestThread() {
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
