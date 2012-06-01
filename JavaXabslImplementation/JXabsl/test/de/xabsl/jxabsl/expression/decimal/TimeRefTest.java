package de.xabsl.jxabsl.expression.decimal;

import static org.junit.Assert.*;

import org.junit.Test;

import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.test.CrashTestDummies;

public class TimeRefTest {

	int time = 0;

	@Test
	public void testGetValue() {

		TimeFunction clock = new TimeFunction() {
			public long getTime() {
				return time;
			}
		};

		TimeRef timeRef = new TimeRef(CrashTestDummies.getDebugMessages(),
				clock);

		assertEquals(0, timeRef.getValue());

		time = 5;

		assertEquals(5, timeRef.getValue());

	}

}
