/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine.deliverytest;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.parameters.EnumeratedParameter;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabsl.symbols.BooleanInputSymbol;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.test.CrashTestDummies;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.io.InputFromField;
import de.xabsl.jxabslx.io.InputFromMethod;
import de.xabsl.jxabslx.io.OutputToField;
import de.xabsl.jxabslx.symbols.DecimalInputSymbolImpl;
import de.xabsl.jxabslx.symbols.DecimalOutputSymbolImpl;
import de.xabsl.jxabslx.symbols.EnumeratedInputSymbolImpl;
import de.xabsl.jxabslx.symbols.JavaEnumeration;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class Robot {

	public enum Direction {
		north, east, south, west
	};

	private int x;
	private int y;
	private Vector2 packet;
	private Direction direction = Direction.north;
	protected World world;

	protected Engine engine;

	public Robot(World world) throws FileNotFoundException,
			NoSuchFieldException, IntermediateCodeMalformedException,
			SecurityException, NoSuchMethodException {

		this.world = world;

		engine = new Engine(CrashTestDummies.getDebugMessages(),
				CrashTestDummies.systemTimeFunction());

		// register symbols

		final Enumeration enumDirection = new JavaEnumeration("Direction",
				Direction.class, CrashTestDummies.getDebugMessages());
		engine.registerEnumeration(enumDirection);

		Class thisClass = this.getClass();

		Field field;

		Method method = thisClass.getMethod("currentPacketX", Integer.TYPE);
		engine.registerDecimalInputSymbol("getPacketX",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{"getPacketX.packet"}, engine, CrashTestDummies
								.getDebugMessages()));

		method = thisClass.getMethod("currentPacketY", Integer.TYPE);
		engine.registerDecimalInputSymbol("getPacketY",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{"getPacketY.packet"}, engine, CrashTestDummies
								.getDebugMessages()));

		method = thisClass.getMethod("currentDestinationX", Integer.TYPE);
		engine.registerDecimalInputSymbol("getDestinationX",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{"getDestinationX.packet"}, engine, CrashTestDummies
								.getDebugMessages()));

		method = thisClass.getMethod("currentDestinationY", Integer.TYPE);
		engine.registerDecimalInputSymbol("getDestinationY",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[]{"getDestinationY.packet"}, engine, CrashTestDummies
								.getDebugMessages()));

		method = thisClass.getMethod("getX");
		engine.registerDecimalInputSymbol("robotX", new DecimalInputSymbolImpl(
				new InputFromMethod(method, this), Conversions
						.getDecimalConversion(method.getReturnType()),
				new String[0], engine, CrashTestDummies.getDebugMessages()));

		method = thisClass.getMethod("getY");
		engine.registerDecimalInputSymbol("robotY", new DecimalInputSymbolImpl(
				new InputFromMethod(method, this), Conversions
						.getDecimalConversion(method.getReturnType()),
				new String[0], engine, CrashTestDummies.getDebugMessages()));

		method = thisClass.getMethod("getDirection");
		engine.registerEnumeratedInputSymbol("robotDirection",
				new EnumeratedInputSymbolImpl(enumDirection,
						new InputFromMethod(method, this),
						Conversions.getEnumeratedConversion(method
								.getReturnType()), new String[0], engine,
						CrashTestDummies.getDebugMessages()));

		method = thisClass.getMethod("getNrPackets");
		engine.registerDecimalInputSymbol("getNrPackets",
				new DecimalInputSymbolImpl(new InputFromMethod(method, this),
						Conversions
								.getDecimalConversion(method.getReturnType()),
						new String[0], engine, CrashTestDummies
								.getDebugMessages()));
		/*
		 * method = thisClass.getMethod("turnWhichWay", Direction.class);
		 * engine.registerBooleanInputSymbol("turnWhichWay", new
		 * BooleanInputSymbolImpl(new InputFromMethod(method, this), Conversions
		 * .getBooleanConversion(method.getReturnType()), new String[] {
		 * "turnWhichWay.to" }, CrashTestDummies.getDebugMessages()));
		 */
		engine.registerBooleanInputSymbol("turnWhichWay",
				new BooleanInputSymbol() {

					Parameters parameters = new Parameters(CrashTestDummies
							.getDebugMessages());
					Object parameter;

					{
						parameters.registerEnumerated("turnWhichWay.to",
								enumDirection, new EnumeratedParameter() {

									// (Java 6) @Override
									public void set(Object value) {
										parameter = value;
									}
								});
					}

					// (Java 6) @Override
					public Parameters getParameters() {
						return parameters;
					}

					// (Java 6) @Override
					public boolean getValue() {

						return turnWhichWay((Direction) parameter);
					}
				});

		// register basic behaviors
		engine.registerBasicBehavior(new BasicBehavior("pickUp",
				CrashTestDummies.getDebugMessages()) {
			@Override
			public void execute() {
				pickUp();
			}
		});

		engine.registerBasicBehavior(new BasicBehavior("leave",
				CrashTestDummies.getDebugMessages()) {
			@Override
			public void execute() {
				leave();
			}
		});

		engine.registerBasicBehavior(new BasicBehavior("turn_left",
				CrashTestDummies.getDebugMessages()) {
			@Override
			public void execute() {
				turnLeft();
			}
		});

		engine.registerBasicBehavior(new BasicBehavior("turn_right",
				CrashTestDummies.getDebugMessages()) {
			@Override
			public void execute() {
				turnRight();
			}
		});

		engine.registerBasicBehavior(new BasicBehavior("move_forward",
				CrashTestDummies.getDebugMessages()) {
			@Override
			public void execute() {
				moveForward();
			}
		});

		method = thisClass.getMethod("directionToPoint", Integer.TYPE,
				Integer.TYPE);
		engine.registerEnumeratedInputSymbol("directionToPoint",
				new EnumeratedInputSymbolImpl(enumDirection,
						new InputFromMethod(method, this),
						Conversions.getEnumeratedConversion(method
								.getReturnType()), new String[] {
								"directionToPoint.x", "directionToPoint.y" },
						engine, CrashTestDummies.getDebugMessages()));

		InputSource input = new ScannerInputSource(new File(
				"test/de/xabsl/jxabsl/engine/deliverytest/intermediatecode"));

		engine.createOptionGraph(input);

	}

	protected Robot() {
	}

	public int currentPacketX(int currentPacket) {
		return world.packets.get(currentPacket).x;
	}

	public int currentPacketY(int currentPacket) {
		return world.packets.get(currentPacket).y;
	}

	public int currentDestinationX(int currentPacket) {
		return world.destinations.get(currentPacket).x;
	}

	public int currentDestinationY(int currentPacket) {
		return world.destinations.get(currentPacket).y;
	}

	public void execute() {
		engine.execute();

	}

	public void turnRight() {
		int ordinal = ((direction.ordinal() + 1) % 4);
		if (ordinal < 0)
			ordinal += 4;
		direction = direction.values()[ordinal];
		System.out.println("Robot: Turning right. Direction is now "
				+ direction);

	}

	public void turnLeft() {
		int ordinal = ((direction.ordinal() - 1) % 4);
		if (ordinal < 0)
			ordinal += 4;
		direction = direction.values()[ordinal];
		System.out
				.println("Robot: Turning left. Direction is now " + direction);

	}

	public void moveForward() {
		System.out
				.println("Robot: Moving forward from (" + x + ", " + y + ") ");
		switch (direction) {
		case north:
			y--;
			break;
		case east:
			x++;
			break;
		case south:
			y++;
			break;
		case west:
			x--;
			break;
		default:
			throw new IllegalStateException(
					"Direction should be between 0 and 3");
		}
		System.out.println(" to (" + x + ", " + y + ")");

		// do we carry anything?
		if (packet != null) {
			System.out.println("Robot: Carrying a packet.");
			packet.x = this.x;
			packet.y = this.y;

		}

	}

	public void pickUp() {
		System.out.println("Robot: Picking up packet.");
		this.packet = world.pickUpAt(x, y);
	}

	public void leave() {
		this.packet = null;
	}

	/**
	 * Calculate the approximate direction of a point as seen from the robot.
	 * 
	 * @return North means straight etc.
	 */
	public Direction directionToPoint(int x, int y) {
		int direction_x = x - this.x;
		int direction_y = y - this.y;

		float angle = (float) Math.atan2(direction_x, -direction_y);

		int ordinal = (int) (Math.round((angle * 4) / (2 * Math.PI)) % 4);
		return ordinal < 0 ? Direction.values()[ordinal + 4] : Direction
				.values()[ordinal];
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getNrPackets() {
		return world.packets.size();
	}

	/**
	 * 
	 * @param from
	 *            Turn from this direction
	 * @param to
	 *            To this direction
	 * @return false: turn right, true: turn left
	 */
	public boolean turnWhichWay(Direction to) {
		return (((to.ordinal() % 4) + 2) > 3);
	}
}
