/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.deliverytest;

import java.util.ArrayList;
import java.util.List;

/**
 * The world in which the robot delivers packets. Holds a list of start- and
 * destination positions.
 */
public class World {

	public List<Vector2> packets;
	public List<Vector2> destinations;

	public World() {
		// initialize some values for testing

		packets = new ArrayList<Vector2>();
		packets.add(new Vector2(4, 5));
		packets.add(new Vector2(1, 1));

		destinations = new ArrayList<Vector2>();
		destinations.add(new Vector2(9, 2));
		destinations.add(new Vector2(2, 2));

	}

	/**
	 * Returns a packet at given location. If there is none, then null.
	 */
	public Vector2 pickUpAt(int x, int y) {
		for (Vector2 p : packets) {
			if (p.x == x && p.y == y)
				return p;
		}
		return null;
	}

	public boolean allPacketsInPlace() {
		return packets.equals(destinations);
	}
}
