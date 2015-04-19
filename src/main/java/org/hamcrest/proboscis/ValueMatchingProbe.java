package org.hamcrest.proboscis;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Useful template for creating single value probes.
 * @param <T> The type of the value.
 */
public abstract class ValueMatchingProbe<T> implements Probe {
    private final Matcher<? super T> criteria;
    private T snapshot;
    
    protected ValueMatchingProbe(Matcher<? super T> criteria) {
        this.criteria = criteria;
    }

    /**
     * Take a snapshot of the value to be probed for.
     * @return The snapshot value.
     */
    protected abstract T snapshotValue();

    /**
     * Describe the meaning of the value
     * @param description Description to write to
     */
    protected abstract void describeValueTo(Description description);


    public void probe() {
        snapshot = snapshotValue();
    }

    public boolean isSatisfied() {
        return criteria.matches(snapshot);
    }

    public void describeTo(Description description) {
        describeValueTo(description);
        description.appendText(" that ")
                   .appendDescriptionOf(criteria);
    }

    public void describeFailureTo(Description description) {
        describeValueTo(description);
        description.appendText(" was ")
                   .appendValue(snapshot);
    }
}
