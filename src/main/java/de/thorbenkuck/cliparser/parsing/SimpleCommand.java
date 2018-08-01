package de.thorbenkuck.cliparser.parsing;

public abstract class SimpleCommand extends AbstractCommand {

	protected SimpleCommand(String identifier) {
		super(identifier);
	}

	protected SimpleCommand(String identifier, String description) {
		super(identifier, description);
	}

	/**
	 * This method call is ignored
	 * {@inheritDoc}
	 */
	@Override
	protected void handle(String[] arguments, CliParser parser) {

	}
}
