package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.Printer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStrategyCliParser extends AbstractCliParser implements StrategyCliParser {

	private ParsingStrategy parsingStrategy;

	protected AbstractStrategyCliParser(Printer printer) {
		super(printer);
	}

	protected AbstractStrategyCliParser(Printer printer, ParsingStrategy parsingStrategy) {
		super(printer);
		setParsingStrategy(parsingStrategy);
	}

	protected synchronized Boolean parse(StringBuilder stringBuilder){
		String enteredText = stringBuilder.toString();
		String command = parsingStrategy.getCommand(stringBuilder);
		for (Command c : getCommands()) {
			if (command.equals(c.getIdentifier())) {
				c.run(parseAllOptions(stringBuilder), this);
				return true;
			}
		}
		printError("Unknown command: " + enteredText);
		return false;
	}

	/**
	 * Nimmt einen String und durchläuft ihn, um ihn nach Options zu überprüfen.
	 * Dabei wird eine Liste mit {@link Option}'s zurück gegeben.
	 *
	 * @param command Der Text, wie er aus dem Eingabe-Feld kam, wrapped in einem StringBuilder
	 * @return Alle Optionen, welche sich in dem AbstractCommand befinden.
	 */
	private ArrayList<Option> parseAllOptions(StringBuilder command) {
		ArrayList<Option> options = new ArrayList<>();
		while (textContainsMoreOptions(command)) {
			options.add(getNextOption(command));
		}
		return options;
	}

	/**
	 * Gibt zurück, ob der String weitere, potentielle Optionen enthält
	 *
	 * @param text der Text, welcher überprüft werden soll
	 * @return true, wenn möglicherweise weiter Optionen im Text sind, sonst false
	 */
	private boolean textContainsMoreOptions(StringBuilder text) {
		return parsingStrategy.hasMoreOptions(text);
	}

	/**
	 * Parsed die nächste Option aus dem AbstractCommand, welcher ihr gegeben wird.
	 *
	 * @param command der AbstractCommand, welcher aus einem Eingabe-Feld stammt
	 * @return die nächste Option
	 */
	private Option getNextOption(StringBuilder command) {
		if (textContainsMoreOptions(command)) {
			return parsingStrategy.getNextOption(command);
		}
		return new Option("", "");
	}

	@Override
	public final void setParsingStrategy(ParsingStrategy parsingStrategy) {
		this.parsingStrategy = parsingStrategy;
	}

	@Override
	public final Boolean parse(String enteredText) {
		try {
			enteredText = preParse(enteredText);
			if(enteredText.equals("")) {
				return false;
			}
			StringBuilder stringBuilder = new StringBuilder(enteredText);
			return parse(stringBuilder);
		} finally {
			setLastCommand(enteredText);
		}
	}

	@Override
	public final List<Option> parseAllOptions(String text) {
		return parseAllOptions(new StringBuilder(text));
	}

	@Override
	public final boolean textContainsMoreOptions(String text) {
		return textContainsMoreOptions(new StringBuilder(text));
	}

	@Override
	public final Option getNextOption(String text) {
		return getNextOption(new StringBuilder(text));
	}
}
