/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.deliverytest;

/**
 * 2-dimensional vector
 */

public class Vector2 {

	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x;

	public int y;

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Vector2) {
			return (x == ((Vector2) obj).x && y == ((Vector2) obj).y);
		} else
			return false;
	}

}
