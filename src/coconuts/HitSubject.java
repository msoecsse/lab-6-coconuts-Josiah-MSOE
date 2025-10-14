// Author: Josiah Mathews
package coconuts;

/**
 * HitSubject interface to model something for Observers to track and latch onto.
 */
public interface HitSubject {
    void attach(HitObserver hitObserver);
    void detach(HitObserver hitObserver);
    void notifyAll(HitEvent hitEvent);
}
