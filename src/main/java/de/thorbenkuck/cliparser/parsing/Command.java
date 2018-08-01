package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.InputReader;

import java.util.List;
import java.util.function.Consumer;

public interface Command {

	static Command defaultStopCommand(InputReader inputReader) {
		return defaultStopCommand("stop", inputReader);
	}

	static Command defaultStopCommand(String identifier, InputReader inputReader) {
		return new DefaultStopCommand(identifier, inputReader);
	}

	static Command defaultListCommand(String identifier) {
		return new DefaultListCommand(identifier);
	}

	static Command defaultListCommand() {
		return defaultListCommand("help");
	}

	void addOption(String optionIdentifier, Consumer<String> optionApplier);

	void addOption(String optionIdentifier, Runnable optionApplier);

	String getIdentifier();

	String getDescription();

	void run(List<Option> options, String[] arguments, CliParser cliParser);

	void doNotEvaluateOptions();

	void doEvaluateOptions();
}
