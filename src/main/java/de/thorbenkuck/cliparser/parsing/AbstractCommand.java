package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.CommandLineInputReader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
	private String identifier;
	private String description;
	private final ArrayList<Option> options;
	private BiConsumer<List<Option>, CliParser> parserConsumer;
	private CliParser cliParser;

	public AbstractCommand(String identifier) {
		this(identifier, "");
	}

	/**
	 * Hier wird ein Comm ohne QueuedAction erstellt.
	 *
	 * @param identifier  der Identifier
	 * @param description die Description
	 */
	public AbstractCommand(String identifier, String description) {
		this(identifier, (option, parser) -> {}, description);
	}
	/**
	 * Erstellt einen Comm, ohne Beschreibung.
	 *
	 * @param identifier   Der Identifier dieses Comm's
	 * @param parserConsumer Die QueuedAction für diesen Comm
	 */
	public AbstractCommand(String identifier, Consumer<List<Option>> parserConsumer) {
		this(identifier, parserConsumer, "");
	}

	/**
	 * Erstellt einen Vollwertigen Comm.
	 * Ein Comm ist genau dann vollwertig, wenn er einen gesetzte Identifier, eine gesetzte Description und QueuedAction besitzt.
	 *
	 * @param identifier   der Identifier dieses Comm's
	 * @param parserConsumer die QueuedAction für diesen Comm
	 * @param description  Die Beschreibung des Comm's
	 */
	public AbstractCommand(String identifier, Consumer<List<Option>> parserConsumer, String description) {
		this.identifier = identifier;
		this.description = description;
		this.parserConsumer = wrapConsumer(parserConsumer);
		this.options = new ArrayList<>();
	}

	/**
	 * Erstellt einen Comm, ohne Beschreibung.
	 *
	 * @param identifier   Der Identifier dieses Comm's
	 * @param parserConsumer Die QueuedAction für diesen Comm
	 */
	public AbstractCommand(String identifier, BiConsumer<List<Option>, CliParser> parserConsumer) {
		this(identifier, parserConsumer, "");
	}

	/**
	 * Erstellt einen Vollwertigen Comm.
	 * Ein Comm ist genau dann vollwertig, wenn er einen gesetzte Identifier, eine gesetzte Description und QueuedAction besitzt.
	 *
	 * @param identifier   der Identifier dieses Comm's
	 * @param parserConsumer die QueuedAction für diesen Comm
	 * @param description  Die Beschreibung des Comm's
	 */
	public AbstractCommand(String identifier, BiConsumer<List<Option>, CliParser> parserConsumer, String description) {
		this.identifier = identifier;
		this.description = description;
		this.parserConsumer = parserConsumer;
		this.options = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "Comm{" +
				"identifier='" + identifier + '\'' +
				", description='" + description + '\'' +
				", options=" + options +
				", parserConsumer=" + parserConsumer +
				'}';
	}

	/**
	 * Der Identifier ist das Identifizierende des Comm's. Er ist der "Befehl" welchen man in eine Eingabe tippt.
	 *
	 * @return den Identifier
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Die Discription des Comm's ist die Doku eben jenes. Sollte hier nicht stehen, ist das nicht wild, kann aber verwirren.
	 *
	 * @return die Description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setConsumer(Consumer<List<Option>> consumer) {
		this.parserConsumer = wrapConsumer(consumer);
	}

	@Override
	public void setConsumer(BiConsumer<List<Option>, CliParser> consumer) {
		this.parserConsumer = consumer;
	}

	/**
	 * Bei Aufruf dieser Methode, führt der Comm seine intern gesetzte QueuedAction aus.
	 */
	@Override
	public void run(List<Option> options, CliParser cliParser) {
		requireConsumer();
		parserConsumer.accept(options, cliParser);
	}

	protected void requireConsumer() {
		if(parserConsumer == null) {
			throw new IllegalStateException("Consumer required!");
		}
	}

	protected final BiConsumer<List<Option>, CliParser> wrapConsumer(Consumer<List<Option>> consumer) {
		return new OptionConsumerWrapper(consumer);
	}

	protected final void runSynchronized(Runnable runnable) {
		synchronized (options) {
			runnable.run();
		}
	}
}
