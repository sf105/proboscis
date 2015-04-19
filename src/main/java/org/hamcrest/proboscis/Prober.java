package org.hamcrest.proboscis;

public interface Prober {
    /**
     * Apply the given probe, which might trigger a failure if it's not satisfied.
     * @param probe The probe to apply.
     */

    void check(Probe probe);
}
