package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.CommandLineInputReader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Ein Command ist ein, von dem {@link } verstandener AbstractCommand.
 * Dieser besteht in Wesentlichen aus 3 Teilen.
 * 1. Dem Identifier.
 * <p>Dieser ist identifizierend für diesen Command, sollte also eindeutig sein.</p>
 * 2. Der Description.
 * <p>Die ist die Doku des Commands. Lässt man sich alle Command vom {@link CommandLineInputReader} auflisten, so ist dies die Erklärung.</p>
 * 3. Dem Consumer.
 * <p>Der {@link Consumer}(bzw {@link BiConsumer}) beschreibt, was passieren soll, wenn der Command ausgeführt wird.</p>
 */
public abstract class AbstractCommand implements Serializable, Command {

	private static final long serialVersionUID = 4414647424220391756L;
	private final String identifier;
	private final String description;
	private final Map<String, Consumer<String>> optionMapping = new HashMap<>();
	private final List<Option> options = new ArrayList<>();
	private final AtomicBoolean evaluateOptions = new AtomicBoolean(true);

	protected AbstractCommand(String identifier) {
		this(identifier, "");
	}

	/**
	 * Hier wird ein Comm ohne QueuedAction erstellt.
	 *
	 * @param identifier  der Identifier
	 * @param description die Description
	 */
	protected AbstractCommand(String identifier, String description) {
		this.identifier = identifier;
		this.description = description;
	}

	protected final void evaluateOptions() {
		for (Option option : options) {
			if (!option.isEmpty()) {
				Consumer<String> consumer;
				synchronized (optionMapping) {
					consumer = optionMapping.get(option.getOptionIdentifier());
				}
				consumer.accept(option.getParameter());
			}
		}
	}

	protected abstract void handle(String[] arguments, CliParser parser);

	@Override
	public final void addOption(String optionIdentifier, Consumer<String> optionApplier) {
		synchronized (optionMapping) {
			optionMapping.put(optionIdentifier, optionApplier);
		}
	}

	@Override
	public final void addOption(String optionIdentifier, Runnable optionApplier) {
		addOption(optionIdentifier, s -> optionApplier.run());
	}

	/**
	 * Der Identifier ist das Identifizierende des Comm's. Er ist der "Befehl" welchen man in eine Eingabe tippt.
	 *
	 * @return den Identifier
	 */
	@Override
	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * Die Discription des Comm's ist die Doku eben jenes. Sollte hier nicht stehen, ist das nicht wild, kann aber verwirren.
	 *
	 * @return die Description
	 */
	@Override
	public final String getDescription() {
		return description;
	}

	/**
	 * Bei Aufruf dieser Methode, führt der Comm seine intern gesetzte QueuedAction aus.
	 */
	@Override
	public final synchronized void run(List<Option> options, String[] arguments, CliParser cliParser) {
		this.options.addAll(options);
		if (evaluateOptions.get()) {
			evaluateOptions();
		}
		handle(arguments, cliParser);
		this.options.clear();
	}

	@Override
	public final void doNotEvaluateOptions() {
		evaluateOptions.set(false);
	}

	@Override
	public final void doEvaluateOptions() {
		evaluateOptions.set(true);
	}

	@Override
	public String toString() {
		return "Command{" +
				"identifier='" + identifier + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
