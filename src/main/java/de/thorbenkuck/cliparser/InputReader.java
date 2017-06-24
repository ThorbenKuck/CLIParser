package de.thorbenkuck.cliparser;

import java.io.IOException;

/**
 * Created by thorben on 24.06.17.
 */
public interface InputReader {
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
	 *
	 * @throws IOException          wenn der Reader unterbrochen wird
	 * @throws InterruptedException wenn das Warten auf Input abgebrochen wird.
	 */
	void start() throws IOException, InterruptedException;

	void setReadyForInputChar(String s);
}
