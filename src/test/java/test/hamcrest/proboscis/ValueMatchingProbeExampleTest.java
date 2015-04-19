package test.hamcrest.proboscis;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.proboscis.UnsynchronizedProber;
import org.hamcrest.proboscis.ValueMatchingProbe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;

/**
 * @author Steve Freeman 2015 http://www.hamcrest.com
 */
public class ValueMatchingProbeExampleTest {
    @Rule public final ExpectedException thrown= ExpectedException.none();

    static class CounterProbe extends ValueMatchingProbe<Integer> {
        int counter = 0;
        public CounterProbe(Matcher<Integer> criteria) { super(criteria); }

        @Override protected Integer snapshotValue() { return counter++; }
        @Override
        protected void describeValueTo(Description description) {
            description.appendText("counter");
        }
    }

    @Test public void
    passesWhenCriteriaMatchesInTime() {
        new UnsynchronizedProber(6, 1).check(
          new CounterProbe(greaterThan(2))
        );
    }


    @Test public void
    failsWhenCriteriaDoesNotMatchInTime() {
        thrown.expectMessage("greater than <100>");

        new UnsynchronizedProber(3, 1).check(
          new CounterProbe(greaterThan(100))
        );
    }

}
