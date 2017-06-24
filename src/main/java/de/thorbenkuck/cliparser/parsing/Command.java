package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.InputReader;

import java.util.List;
import java.util.function.BiConsumer;
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

	String getIdentifier();

	String getDescription();

	void setConsumer(Consumer<List<Option>> consumer);

	void setConsumer(BiConsumer<List<Option>, CliParser> consumer);

	void run(List<Option> options, CliParser cliParser);
}
