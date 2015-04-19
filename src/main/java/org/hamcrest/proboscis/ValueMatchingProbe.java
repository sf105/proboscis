package org.hamcrest.proboscis;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public abstract class ValueMatchingProbe<T> implements Probe {
    private final Matcher<? super T> criteria;
    private T snapshot;
    
    protected ValueMatchingProbe(Matcher<? super T> criteria) {
        this.criteria = criteria;
    }

    protected abstract T snapshotValue();
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
