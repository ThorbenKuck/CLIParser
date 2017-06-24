package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.Printer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;

public abstract class AbstractCliParser implements CliParser {

	private Printer printer;
	private String lastCommand;
	private final List<Command> commandList = new ArrayList<>();
	private final Queue<BiConsumer<StringBuilder, String>> preParsingQueue = new LinkedList<>();

	protected AbstractCliParser(Printer printer) {
		this.printer = printer;
	}

	@Override
	public final void setPrinter(Printer printer) {
		this.printer = printer;
	}

	@Override
	public final void addCommand(Command comm) {
		commandList.add(comm);
	}

	@Override
	public final List<Command> getCommands() {
		return new ArrayList<>(commandList);
	}

	@Override
	public final String getLastCommand() {
		return lastCommand;
	}

	@Override
	public final void print(String string) {
		printer.println(string);
	}

	@Override
	public final void printError(String string) {
		printer.printError(string);
	}

	protected void setLastCommand(String lastCommand) {
		this.lastCommand = lastCommand;
	}

	/**
	 * The String, injected into the BiConsumer is the original entered text.
	 *
	 * @param preParser
	 */
	@Override
	public final void addPreParser(BiConsumer<StringBuilder, String> preParser) {
		preParsingQueue.add(preParser);
	}

	@Override
	public final String preParse(String string) {
		StringBuilder stringBuilder = new StringBuilder(string);
		preParsingQueue.forEach(stringConsumer -> stringConsumer.accept(stringBuilder, string));

		return stringBuilder.toString();
	}
}
