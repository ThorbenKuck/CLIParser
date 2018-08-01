package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.InputReader;

class DefaultStopCommand extends AbstractCommand {

	private final InputReader inputReader;

	DefaultStopCommand(String identifier, InputReader inputReader) {
		super(identifier, "Stops the CommandLineInputReader");
		this.inputReader = inputReader;
	}

	@Override
	protected void handle(String[] arguments, CliParser parser) {
		inputReader.stop();
	}
}
