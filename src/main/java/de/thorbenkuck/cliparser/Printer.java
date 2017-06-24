package de.thorbenkuck.cliparser;

public interface Printer {

	static Printer getDefault() {
		return new DefaultPrinter();
	}

	void print(String string);

	void println(String string);

	default void printError(String string) {
		print(string);
	}

}
