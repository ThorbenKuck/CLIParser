package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.Printer;

import java.util.List;
import java.util.function.BiConsumer;

public interface CliParser extends Parser<String, Boolean> {

	static CliParser getDefault(Printer printer) {
		return new StrategyBasedCliParser(printer);
	}

	void setPrinter(Printer printer);

	void addCommand(Command comm);

	List<Command> getCommands();

	List<Option> parseAllOptions(String text);

	boolean textContainsMoreOptions(String text);

	Option getNextOption(String text);

	String getLastCommand();

	void print(String string);

	void printError(String string);

	void addPreParser(BiConsumer<StringBuilder, String> preParser);

	String preParse(String string);
}
