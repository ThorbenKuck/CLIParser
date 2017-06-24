package de.thorbenkuck.cliparser;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractInputReader implements InputReader {

	private String readyForInputChar = "$";
	private CountDownLatch countDownLatch = new CountDownLatch(0);
	private final Lock countDownLatchLock = new ReentrantLock();
	private String prefix;
	protected boolean waitingForInput;
	private Printer printer;
	private boolean running;

	protected AbstractInputReader(Printer printer) {
		this.printer = printer;
	}

	@Override
	public final void letMeWait() {
		try {
			tryResetCountDownLatch();
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			countDownLatchLock.unlock();
		}
	}

	private void tryResetCountDownLatch() {
		countDownLatchLock.lock();
		countDownLatch = new CountDownLatch(1);
	}

	@Override
	public final void letMeResume() {
		countDownLatch.countDown();
	}

	@Override
	public final void stop() {
		running = false;
		letMeResume();
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
	 *
	 * @throws IOException          wenn der Reader unterbrochen wird
	 * @throws InterruptedException wenn das Warten abgebrochen wird.
	 */
	@Override
	public final void start() throws IOException, InterruptedException {
		running = true;
		printStartMessage();
		while (running) {
			try {
				countDownLatch.await(10, TimeUnit.SECONDS);
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
			} catch(IOException e) {
				printMessage("Error while reading!");
				e.printStackTrace();
			} catch (Exception e) {
				printMessage("\n\nUnexpected error!!!!\n\n#-----------------#\n" + e.getClass().getSimpleName() + "\n", false);
				e.printStackTrace(System.out);
				printMessage("\n#-----------------#\n\n", false);
			}
		}
		printMessage("CommandLineInputReader shutdown!");
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

	protected abstract void executeCommand(String line);

	protected abstract String getNextString() throws InterruptedException, IOException;

	protected abstract void printStartMessage();

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


	@Override
	public final void setReadyForInputChar(String s) {
		this.readyForInputChar = s;
	}
}
