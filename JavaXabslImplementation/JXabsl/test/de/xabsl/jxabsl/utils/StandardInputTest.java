/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.utils;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

import de.xabsl.jxabslx.utils.ScannerInputSource;

public class StandardInputTest {

	@Test
	public void testScannerInput() throws FileNotFoundException {

		ScannerInputSource si = new ScannerInputSource(new File(
				"test/de/xabsl/jxabsl/utils/testInput"));

		Assert.assertEquals("4", si.next());
		Assert.assertEquals("5", si.next());
		Assert.assertEquals("6", si.next());
		Assert.assertEquals("test", si.next());
		Assert.assertEquals("9", si.next());
		Assert.assertEquals(10, si.nextInt());
		Assert.assertEquals(true, si.nextBoolean());
		Assert.assertEquals(false, si.nextBoolean());

		Assert.assertEquals(3.14, si.nextDouble(), 0.1);
		Assert.assertEquals("end", si.next());

	}
}
