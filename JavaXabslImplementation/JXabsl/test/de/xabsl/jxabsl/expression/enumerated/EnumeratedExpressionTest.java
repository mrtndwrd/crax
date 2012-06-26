/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.expression.enumerated;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnumeratedExpressionTest {

	@Test
	public void testEnumerationFromIntermediateCode() {
		assertEquals("enumeration", EnumeratedExpression
				.enumerationFromIntermediateCode("enumeration.name"));
		assertEquals("enumeration.more", EnumeratedExpression
				.enumerationFromIntermediateCode("enumeration.more.name"));

	}

	@Test
	public void testElementFromIntermediateCode() {
		assertEquals("name", EnumeratedExpression
				.elementFromIntermediateCode("enumeration.name"));
		assertEquals("name", EnumeratedExpression
				.elementFromIntermediateCode("enumeration.more.name"));

	}

}
