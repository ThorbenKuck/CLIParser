package de.thorbenkuck.cliparser.parsing;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Diese Klasse stelle eine Implementierung der Comm's da, damit diese ohne weiter konkrete Implementierung des Comm implementier werden können.
 * Dabei werden die selben 3 Konstruktoren wie im Comm bereit gestellt.
 *
 * @see AbstractCommand
 * {@inheritDoc}
 */
public class DefaultCommand extends AbstractCommand {

	/**
	 * {@inheritDoc}
	 */
	public DefaultCommand(String identifier, Consumer<List<Option>> queuedAction) {
		super(identifier, queuedAction);
	}

	/**
	 * {@inheritDoc}
	 */
	public DefaultCommand(String identifier, String description) {
		super(identifier, description);
	}

	/**
	 * {@inheritDoc}
	 */
	public DefaultCommand(String identifier, Consumer<List<Option>> consumer, String description) {
		super(identifier, description);
		setConsumer(consumer);
	}

	/**
	 * {@inheritDoc}
	 */
	public DefaultCommand(String identifier, BiConsumer<List<Option>, CliParser> consumer) {
		super(identifier);
		setConsumer(consumer);
	}

	/**
	 * {@inheritDoc}
	 */
	public DefaultCommand(String identifier, BiConsumer<List<Option>, CliParser> consumer, String description) {
		super(identifier, description);
		setConsumer(consumer);
	}

}
