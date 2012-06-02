/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabslx.engine.deliverytestreflection;

import java.io.FileNotFoundException;

import org.junit.Test;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.engine.deliverytest.World;

public class DeliveryTestReflection {

	@Test(timeout=2000)
	public void runTest() throws FileNotFoundException, NoSuchFieldException,
			IntermediateCodeMalformedException, SecurityException,
			NoSuchMethodException {
		World world = new World();
		ReflectionRobot robot = new ReflectionRobot(world);

		while (!world.allPacketsInPlace()) {
			robot.execute();
		}
	}
}
