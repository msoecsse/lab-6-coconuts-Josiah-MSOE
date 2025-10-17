// Author: Josiah Mathews
package coconuts;

// An abstraction of all objects that can be hit by another object
// This captures the Subject side of the Observer pattern; observers of the hit event will take action
//   to process that event
// This is a domain class; do not introduce JavaFX or other GUI components here
public class HitEvent {
    IslandObject islandObject1;
    IslandObject islandObject2;

    public HitEvent(IslandObject islandObject1, IslandObject islandObject2) {
        this.islandObject1 = islandObject1;
        this.islandObject2 = islandObject2;
    }
}
