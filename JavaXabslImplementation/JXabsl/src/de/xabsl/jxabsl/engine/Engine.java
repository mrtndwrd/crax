/**
 * XABSL Java implementation
 * 
 * @author Moritz Wissenbach (m.wissenbach@stud.tu-darmstadt.de)
 */

package de.xabsl.jxabsl.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import de.xabsl.jxabsl.EngineInitializationException;
import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.TimeFunction;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.action.ActionBehavior;
import de.xabsl.jxabsl.action.ActionOption;
import de.xabsl.jxabsl.agent.Agent;
import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.behavior.Behavior;
import de.xabsl.jxabsl.behavior.Option;
import de.xabsl.jxabsl.expression.enumerated.EnumeratedExpression;
import de.xabsl.jxabsl.state.State;
import de.xabsl.jxabsl.symbols.BooleanOutputSymbol;
import de.xabsl.jxabsl.symbols.DecimalOutputSymbol;
import de.xabsl.jxabsl.symbols.EnumeratedOutputSymbol;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.utils.DebugMessages;
import de.xabsl.jxabsl.utils.InputSource;

/**
 * Executes a behavior that was specified in the Xabsl language.
 */

public class Engine extends Symbols {

	private LinkedHashMap<String, Agent> agents = new LinkedHashMap<String, Agent>();
	private LinkedHashMap<String, BasicBehavior> basicBehaviors = new LinkedHashMap<String, BasicBehavior>();
	private boolean initialized;
	private List<Double> internalDecimalValues = new ArrayList<Double>();
	private List<Boolean> internalBooleanValues = new ArrayList<Boolean>();
	private List<Object> internalEnumeratedValues = new ArrayList<Object>();
	private LinkedHashMap<String, Option> options = new LinkedHashMap<String, Option>();
	private TimeFunction timeFunction;
	private ActionBehavior rootAction;
	private Agent selectedAgent;

	/**
	 * Constructor
	 * 
	 * @param debug
	 *            Print debuggin information
	 * @param timeFunction
	 *            A function that returns the system time in ms.
	 */

	public Engine(DebugMessages debug, TimeFunction timeFunction) {
		super(debug);
		initialized = false;
		this.timeFunction = timeFunction;

	}

	/**
	 * Executes the engine for the selected agent starting from the root option.
	 * (Including the selected basic behavior)
	 */

	public void execute() {

		if (!initialized)
			throw new EngineInitializationException(
					"Call createOptionGraph() before first execute.");

		rootAction.execute();

		for (Option o : options.values()) {
			o.setWasActive(o.isActive());
			o.setActive(false);
		}

		for (BasicBehavior b : basicBehaviors.values()) {
			b.setWasActive(b.isActive());
			b.setActive(false);
		}
	}

	/**
	 * Reads the intermediate code from an input source and creates the option
	 * graph. Note that the basic behaviors and symbols have to be registered
	 * before this function is called.
	 */

	public void createOptionGraph(InputSource input)
			throws IntermediateCodeMalformedException {
		int i;

		if (initialized) {
			throw new EngineInitializationException(
					"Do not call this function twice!");

		}

		// create internal enumerations

		int numberOfInternalEnumerations = input.nextInt();

		for (i = 0; i < numberOfInternalEnumerations; i++) {
			String enumerationName = input.next();
			int numberOfElements = input.nextInt();
			for (int j = 0; j < numberOfElements; j++) {
				String elementName = EnumeratedExpression
						.elementFromIntermediateCode(input.next());
				registerInternalEnumElement(enumerationName, elementName);
			}
		}

		// create internal symbols
		int numberOfInternalSymbols = input.nextInt();

		for (i = 0; i < numberOfInternalSymbols; i++) {
			String type = input.next();
			switch (type.charAt(0)) {
			case 'd': {
				String name = input.next();
				addInternalDecimalSymbol(name);
				registerDecimalOutputSymbol(name,

				// Output symbol to modify internal values
						new DecimalOutputSymbol() {

							private int pos = internalDecimalValues.size() - 1;

							public double getValue() {
								return internalDecimalValues.get(pos);
							}

							public void setValue(double value) {
								internalDecimalValues.set(pos, value);
							}
						});

				break;
			}
			case 'b': {
				String name = input.next();
				addInternalBooleanSymbol(name);
				registerBooleanOutputSymbol(name,

				// Output symbol to modify internal values
						new BooleanOutputSymbol() {

							private int pos = internalBooleanValues.size() - 1;

							public boolean getValue() {
								return internalBooleanValues.get(pos);
							}

							public void setValue(boolean value) {
								internalBooleanValues.set(pos, value);
							}
						});

				break;
			}
			case 'e': {
				String enumerationName = input.next();
				final Enumeration enumeration = getEnumeration(enumerationName);

				if (enumeration.getNrElements() == 0)
					throw new EngineInitializationException(
							"No enumeration elements for " + enumerationName
									+ " were registered");

				String name = input.next();
				addInternalEnumeratedSymbol(name);
				registerEnumeratedOutputSymbol(name,

				new EnumeratedOutputSymbol() {

					private int pos = internalEnumeratedValues.size() - 1;

					public Object getValue() {
						return internalDecimalValues.get(pos);
					}

					public void setValue(Object value) {
						internalEnumeratedValues.set(pos, value);
					}

					// (Java 6) @Override
					public Enumeration getEnumeration() {

						return enumeration;
					}

				});

				break;
			}

			}
		}
		
		// the total number of options in the intermediate code
		int numberOfOptions = input.nextInt();

		// create empty options
		for (i = 0; i < numberOfOptions; i++) {
			String optionName = input.next();
			options.put(optionName, new Option(optionName, input, debug, this,
					timeFunction));
		}

		debug.printlnInit("Registered " + i + " options");


		// create the options and their states

		Iterator<Option> optionIterator = options.values().iterator();

		while (optionIterator.hasNext()) {
			Option o = optionIterator.next();
			o.create(input, options, this);
		}

		// create the agents
		int numberOfAgents = input.nextInt();

		for (i = 0; i < numberOfAgents; i++) {
			String agentName = input.next();
			String rootOption = input.next();
			agents.put(agentName, new Agent(agentName, options.get(rootOption),
					debug));
		}
		// check for loops in the option graph
		Iterator<Agent> agentIterator = agents.values().iterator();
		while (agentIterator.hasNext()) {
			Agent a = agentIterator.next();
			if (a.getRootOption() instanceof Option) {
				List<Option> currentOptionPath = new ArrayList<Option>();
				currentOptionPath.add(((Option) a.getRootOption()));

				// recursively call the checkForLoops function
				try {

					checkForLoops(currentOptionPath);
				} catch (IntermediateCodeMalformedException e) {

					throw new IntermediateCodeMalformedException(
							"The created option graph contains loops", e);

				}
			}
		}
		
		//FIXME cooperating states
		
		selectedAgent = agents.values().iterator().next();
		setRootAction();
		initialized = true;

	}

	/**
	 * A recursive function that is used to check for loops in the option graph.
	 * 
	 * @param currenOptionPath
	 *            An array of the currently traced option path
	 * @param currentDepth
	 *            The depth of the current option path
	 * @throws IntermediateCodeMalformedException
	 *             if the option graph contains a loop.
	 */

	private void checkForLoops(List<Option> currentOptionPath)
			throws IntermediateCodeMalformedException {
		int i, j, k;

		Option currentOption = currentOptionPath
				.get(currentOptionPath.size() - 1);

		Iterator<State> si = currentOption.getStates().values().iterator();
		while (si.hasNext()) {
			State s = si.next();
			for (j = 0; j < s.getActions().size(); j++) {
				if (s.getActions().get(j) instanceof ActionOption) {
					ActionOption nextAction = (ActionOption) (s.getActions()
							.get(j));
					for (Option o : currentOptionPath) {
						// check for the subsequent option of each state
						// weather the referenced
						// option is contained in the current option path
						if ((nextAction).getBehavior() == o) {

							throw new IntermediateCodeMalformedException(
									"state "
											+ s.getName()
											+ " in option "
											+ currentOption.getName()
											+ " references subsequent option "
											+ nextAction.getBehavior()
													.getName()
											+ ". But option "
											+ currentOption.getName()
											+ " is also directly or indirectly referenced by option "
											+ nextAction.getBehavior()
													.getName());
						}

					}
					// recursion
					// TODO use only one list instead of duplicating
					List<Option> branch = new ArrayList<Option>(
							currentOptionPath);
					branch.add((Option) nextAction.getBehavior());

					checkForLoops(branch);
				}
			}
		}
	}

	/**
	 * Registers a basic behavior at the engine. This must be done before the
	 * intermediate code is read.
	 * 
	 * @param basicBehavior
	 *            A reference to the basic behavior
	 */

	public void registerBasicBehavior(BasicBehavior basicBehavior) {
		debug.printlnInit("Registering basic behavior "
				+ basicBehavior.getName());
		if (basicBehaviors.containsKey(basicBehavior.getName()))
			throw new EngineInitializationException("Basic behavior "
					+ basicBehavior.getName() + " was already registered");

		basicBehaviors.put(basicBehavior.getName(), basicBehavior);
	}

	/**
	 * Executes the option graph starting from a given option or basic behavior.
	 * Can be called to test a single option or basic behavior.
	 * 
	 * @param name
	 *            The name of the option or basic behavior
	 * @param isOption
	 *            True for an option, false for a basic behavior
	 * @return When false, the option is not known to the engine
	 */

	public boolean setRootAction(String name, boolean isOption) {
		if (isOption) {
			// check if the option exists
			if (!options.containsKey(name))
				return false;

			// set the current root option to the requested option
			setRootAction(options.get(name));

		} else {
			// check if the basic behavior exists
			if (!basicBehaviors.containsKey(name))
				return false;

			// set the current root option to the requested option
			setRootAction(basicBehaviors.get(name));

		}

		return true;
	}

	/**
	 * Sets the root option of the selected agent
	 */

	public void setRootAction() {
		setRootAction(selectedAgent.getRootOption());
	}

	/**
	 * Sets the root option to the specified option or basic behavior
	 */

	public void setRootAction(Behavior behavior) {
		rootAction = Action.create(behavior, debug, timeFunction);
	}

	/** Returns the selected root action */

	public ActionBehavior getRootAction() {
		return rootAction;
	}

	/**
	 * Returns the selected root option, return 0 if root action is not an
	 * option
	 */

	public Option getRootOption() {
		return (Option) ((ActionOption) rootAction).getBehavior();
	}

	/** Returns the name of the selected agent */

	public String getSelectedAgentName() {
		return selectedAgent.getName();
	}

	/**
	 * Sets the selected Agent. If the last selected agent was different from
	 * the new one, the root option is changed depending on the new agent.
	 * 
	 * @param name
	 *            The name of the agent
	 * @return if the requested agent exists
	 */

	public boolean setSelectedAgent(String name) {
		if (!agents.containsKey(name)) {
			return false;
		}

		Agent newAgent = agents.get(name);

		if (selectedAgent != newAgent) {
			selectedAgent = newAgent;
			setRootAction();
		}
		return true;
	}

	/**
	 * Resets all active options. Next cycle will execute initial state of
	 * currently set root option.
	 */

	public void reset() {
		Iterator<Option> io = options.values().iterator();
		while (io.hasNext()) {
			Option o = io.next();
			o.setWasActive(false);

		}

		Iterator<BasicBehavior> ib = basicBehaviors.values().iterator();
		while (ib.hasNext()) {
			ib.next().setWasActive(false);
		}
	}

	/**
	 * Returns the registered basic behavior given by a name and a set of
	 * parameter names
	 */

	public BasicBehavior getBasicBehavior(String name,
			Set<String> decimalParameterNames,
			Set<String> booleanParameterNames,
			Set<String> enumeratedParameterNames) {

		if (basicBehaviors.containsKey(name)) {
			return basicBehaviors.get(name);
		} else {
			throw new SymbolNotRegisteredException(
					"No basic behavior of the name " + name
							+ " has been registered");
		}
	}

	/**
	 * For debugging.
	 * 
	 * @return All basic behaviors of the engine.
	 */

	public LinkedHashMap<String, BasicBehavior> getBasicBehaviors() {
		return basicBehaviors;
	}

	/**
	 * @return all options of the engine
	 * 
	 */
	public LinkedHashMap<String, Option> getOptions() {
		return options;
	}

	/**
	 * @return all agents of the engine
	 */
	public LinkedHashMap<String, Agent> getAgents() {
		return agents;
	}
	
	
	private void addInternalDecimalSymbol(String name) {
		internalDecimalValues.add(0d);
	}

	private void addInternalBooleanSymbol(String name) {
		internalBooleanValues.add(false);
	}

	private void addInternalEnumeratedSymbol(String name) {
		internalEnumeratedValues.add(null);
	}




}
