package de.thorbenkuck.cliparser;

import de.thorbenkuck.cliparser.parsing.AbstractCommand;
import de.thorbenkuck.cliparser.parsing.CliParser;
import de.thorbenkuck.cliparser.parsing.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Der CommandLineInputReader ist die Schnittstelle zwischen Console und eingabe.
 * Sie signalisiert (ähnlich wie eine *nix-shell) wenn sie bereit ist für eine Eingabe, sowie den Nutzer und die
 * aktuelle Machine, zu welcher dieser Nutzer angemeldet ist.
 */
public class CommandLineInputReader extends AbstractInputReader {

	private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	private Parser<String, Boolean> cliParser;

	/**
	 * Erstellt einen CommandLineInputReader und setzt intern den StopCommand.
	 * Der StopCommand ist ein {@link AbstractCommand}, welcher diesen CommandLineInputReader beendet.
	 */
	public CommandLineInputReader() {
		this(Printer.getDefault());
	}

	public CommandLineInputReader(Printer printer) {
		this(printer, CliParser.getDefault(printer));
	}

	public CommandLineInputReader(Printer printer, CliParser cliParser) {
		super(printer);
		this.cliParser = cliParser;
	}

	/**
	 * Wartet auf die Eingabe von einem Nutzer.
	 *
	 * @throws IOException wenn er unterbrochen wird dabei.
	 */
	private String readLine() throws IOException {
		return bufferedReader.readLine();
	}

	/**
	 * Führt einen AbstractCommand aus, nachdem dieser ausgelesen wurde.
	 *
	 * @param command der String (im Klartext) welcher ausgelesen wurde.
	 */
	protected void executeCommand(String command) {
		cliParser.parse(command);
	}

	@Override
	protected String getNextString() throws InterruptedException, IOException {
		return readLine();
	}

	@Override
	public String toString() {
		return "CommandLineInputReader{" +
				"bufferedReader=" + bufferedReader +
				", cliParser=" + cliParser +
				'}';
	}
}
