/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.deliverytest;

import java.io.FileNotFoundException;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;

/**
 * A test for the complete engine (not really a unit test) A robot delivers
 * packages.
 * 
 * 
 */
public class DeliveryTest {

	@Test (timeout=2000)
	public void runTest() throws FileNotFoundException, NoSuchFieldException,
			IntermediateCodeMalformedException, SecurityException,
			NoSuchMethodException {
		World world = new World();
		Robot robot = new Robot(world);

		while (!world.allPacketsInPlace()) {
			robot.execute();
		}
	}
}
