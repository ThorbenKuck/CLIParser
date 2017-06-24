package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.Printer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Diese Klasse ist eine Klasse, welche genutzt werden kann, um aus Strings, welche in eine "CommandLine" eingegeben
 * wurden, den Befehl und alle Options zu Extrahieren und.
 * Die Befehle müssen dabei registriert werden, damit diese Klasse sie kennt.
 * Optionen werden dann dynamisch geparsed und dem Befehl übergeben.
 * Leider ist es nicht möglich, in dem Konstruktor eines Befehls diese Optionen direkt aus zu lesen, da die effektiv
 * erst NACH dem Aufruf des Konstruktors existieren.
 * Außerdem sollte angemerkt werden, dass diese Klasse absolut nicht für Multi-Threaded-Environments geignet ist.
 * Dafür bedarf es einer Aggregator-Klasse, welche Zufriffe Synchronisiert.
 */
public class StrategyBasedCliParser extends AbstractStrategyCliParser {

	private final long serialVersionUID = 1534142265962329248L;
	private ParsingStrategy parsingStrategy = ParsingStrategy.getDefault();

	public StrategyBasedCliParser(Printer printer, ParsingStrategy parsingStrategy) {
		super(printer, parsingStrategy);
	}

	public StrategyBasedCliParser(ParsingStrategy parsingStrategy) {
		this(Printer.getDefault(), parsingStrategy);
	}

	public StrategyBasedCliParser(Printer printer) {
		this(printer, ParsingStrategy.getDefault());
	}

	public StrategyBasedCliParser() {
		this(Printer.getDefault(), ParsingStrategy.getDefault());
	}

	@Override
	public String toString() {
		return "StrategyBasedCliParser{" +
				"parsingStrategy=" + parsingStrategy + "\'" +
				'}';
	}
}
