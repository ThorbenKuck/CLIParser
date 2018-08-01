package de.thorbenkuck.cliparser.parsing;

public interface ParsingStrategy {

	static ParsingStrategy getDefault() {
		return new ModifyingSplittingParsingStrategy();
	}

	boolean hasMoreOptions(StringBuilder enteredText);

	String getCommand(StringBuilder enteredText);

	Option getNextOption(StringBuilder enteredText);

	String[] getArguments(StringBuilder stringBuilder);

}
