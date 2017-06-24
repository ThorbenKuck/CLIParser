package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.InputReader;

class DefaultStopCommand extends AbstractCommand {
	DefaultStopCommand(String identifier, InputReader inputReader) {
		super(identifier, (options -> inputReader.stop()),"Stops the CommandLineInputReader");
	}
}
