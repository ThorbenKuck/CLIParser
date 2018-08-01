package de.thorbenkuck.cliparser.parsing;

public abstract class NonArgsCommand extends AbstractCommand {

	protected NonArgsCommand(String identifier) {
		super(identifier);
	}

	protected NonArgsCommand(String identifier, String description) {
		super(identifier, description);
	}

	protected abstract void handle(CliParser parser);

	@Override
	protected void handle(String[] arguments, CliParser parser) {
		handle(parser);
	}
}
