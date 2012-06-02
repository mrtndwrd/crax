/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.symbols;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabslx.symbols.JavaEnumeration;

public class EnumerationTest {

	// Test dummy
	enum MoonPhases {
		NEW, FIRSTQUARTER, FULL, THIRDQUARTER
	};

	// Test dummy
	enum SomeEnum {
		SOMEVALUE
	};

	@Test
	public void testEnvironmentEnumeration() {

		Enumeration moonPhases = new JavaEnumeration("MoonPhases",
				MoonPhases.class, CrashTestDummies.getDebugMessages());

		assertTrue(moonPhases.getElementName(MoonPhases.NEW) != null);
		assertFalse(moonPhases.getElementName(SomeEnum.SOMEVALUE) != null);

		assertTrue(moonPhases.getElement("THIRDQUARTER") == MoonPhases.THIRDQUARTER);
		assertNull(moonPhases.getElement("UnfortunatelyNotHere"));
	}

	@Test
	public void testInternalEnumeration() {

		Enumeration moonPhases = new Enumeration("MoonPhases", null);

		moonPhases.add("NEW");
		moonPhases.add("FIRSTQUARTER");
		moonPhases.add("FULL");
		moonPhases.add("THIRDQUARTER");

		assertTrue(moonPhases.getElement("FULL") != null);
		assertFalse(moonPhases.getElement("ECLIPSE") != null);
	}

}
