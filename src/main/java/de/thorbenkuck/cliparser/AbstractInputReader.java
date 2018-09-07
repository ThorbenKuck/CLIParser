package de.thorbenkuck.cliparser;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public abstract class AbstractInputReader implements InputReader {

	private final Semaphore semaphore = new Semaphore(1);
	protected boolean waitingForInput;
	private String readyForInputChar = "$";
	private String startupMessage = "\n#----------------------------------#" +
			"\n#Started the CommandLineInputReader#" +
			"\n#----------------------------------#";
	private String prefix;
	private Printer printer;
	private boolean running;

	protected AbstractInputReader(Printer printer) {
		this.printer = printer;
	}

	private void printStartMessage() {
		printMessage(startupMessage);
	}

	@Override
	public final void letMeWait() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void letMeResume() {
		semaphore.release();
	}

	/**
	 * Diese Methode ist eine kürzere Schreibweise für printMessage(message, true);
	 * D.h. sie gibt den Prefix mit aus.
	 *
	 * @param message die Nachricht, welche Ausgegeben werden soll.
	 */
	@Override
	public final void printMessage(String message) {
		printMessage(message, true);
	}

	@Override
	public final void stop() {
		running = false;
		letMeResume();
	}

	@Override
	public final void setStartupMessage(String startupMessage) {
		this.startupMessage = startupMessage;
	}

	/**
	 * Setzt den prefix für die Console-Ausgabe.
	 *
	 * @param prefix Der Prefix, welcher für die Console nun verwendet werden soll
	 */
	@Override
	public final void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Startet den CommandLineInputReader.
	 * Dabei werden einige Abhängigkeiten erstellt.
	 * Sollte ein Fehler auftreten, so wird dieser durch gereicht. Dies kännte abgefangen werden, muss aber nicht.
	 */
	@Override
	public final void start() {
		running = true;
		printStartMessage();
		while (running) {
			try {
				semaphore.acquire();
				printInputSignal();
				waitingForInput = true;
				String line = getNextString();
				printMessage("", false);
				waitingForInput = false;
				executeCommand(line);
			} catch (InterruptedException e) {
				e.printStackTrace();
				printMessage("Stopping because of Thread interruption!");
				stop();
			} catch (IOException e) {
				printMessage("Error while reading!");
				e.printStackTrace();
			} catch (Exception e) {
				printMessage("\n\nUnexpected error!\n#-----------------#\n" + e.getClass().getSimpleName() + "\n", false);
				e.printStackTrace(System.out);
				printMessage("\n#-----------------#\n\n", false);
				stop();
			} finally {
				semaphore.release();
			}
		}
		printMessage("CommandLineInputReader shutdown!");
	}

	@Override
	public final void setReadyForInputChar(String s) {
		this.readyForInputChar = s;
	}

	protected abstract void executeCommand(String line);

	protected abstract String getNextString() throws InterruptedException, IOException;

	/**
	 * Diese Methode zentralisiert die Ausgabe an die Console.
	 * Dadurch kann hier u.a. zukünftig zentral geloggt werden.
	 *
	 * @param message    die Nachricht, welche Ausgegeben werden soll
	 * @param withPrefix boolean, ob der Prefix mit ausgegeben werden soll, oder nicht.
	 */
	protected final synchronized void printMessage(String message, boolean withPrefix) {
		if (withPrefix) {
			printer.print((prefix != null ? prefix : "") + message);
		} else {
			printer.print(message);
		}
	}

	/**
	 * Durch den Aufruf dieser Methode signalisiert der CommandLineInputReader, dass er für eine Eingabe bereit ist.
	 */
	protected void printInputSignal() {
		printMessage("\n" + readyForInputChar + " ", true);
	}
}
