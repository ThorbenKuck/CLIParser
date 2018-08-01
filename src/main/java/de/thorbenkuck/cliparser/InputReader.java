package de.thorbenkuck.cliparser;

import de.thorbenkuck.cliparser.parsing.BlockingQueueInputReader;
import de.thorbenkuck.cliparser.parsing.CliParser;

import java.util.concurrent.BlockingQueue;

public interface InputReader {

	static InputReader commandLine(CliParser cliParser) {
		return commandLine(Printer.getDefault(), cliParser);
	}

	static InputReader commandLine(Printer printer, CliParser cliParser) {
		return new CommandLineInputReader(printer, cliParser);
	}

	static InputReader blockingQueue(CliParser cliParser, BlockingQueue<String> blockingQueue) {
		return blockingQueue(Printer.getDefault(), cliParser, blockingQueue);
	}

	static InputReader blockingQueue(Printer printer, CliParser cliParser, BlockingQueue<String> blockingQueue) {
		return new BlockingQueueInputReader(printer, cliParser, blockingQueue);
	}

	/**
	 * Diese Methode stoppt die Eingabe kurzzeitig.
	 * Bis die Methode {@link #letMeResume()} getriggert wird.
	 * Hierbei wird intern ein CountDownLatch erstellt, auf welchen die Eingabe wartet.
	 */
	void letMeWait();

	/**
	 * Diese Methode lässt die Eingabeaufforderung wieder weiter vortfahren.
	 * Dabei kann, muss aber nicht {@link #letMeWait()} aufgerufen worden sein.
	 */
	void letMeResume();

	/**
	 * Diese Methode ist eine kürzere Schreibweise für printMessage(message, true);
	 * D.h. sie gibt den Prefix mit aus.
	 *
	 * @param message die Nachricht, welche Ausgegeben werden soll.
	 */
	void printMessage(String message);

	void stop();

	void setStartupMessage(String startupMessage);

	/**
	 * Setzt den prefix für die Console-Ausgabe.
	 *
	 * @param prefix Der Prefix, welcher für die Console nun verwendet werden soll
	 */
	void setPrefix(String prefix);

	/**
	 * Startet den CommandLineInputReader.
	 * Dabei werden einige Abhängigkeiten erstellt.
	 * Sollte ein Fehler auftreten, so wird dieser durch gereicht. Dies kännte abgefangen werden, muss aber nicht.
	 */
	void start();

	void setReadyForInputChar(String s);
}
