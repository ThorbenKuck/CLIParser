package de.thorbenkuck.cliparser.parsing;

import java.util.function.BiConsumer;

public interface PreParser extends BiConsumer<StringBuilder, CliParser> {

	static PreParser bangBang() {
		return new BangBangPreParser();
	}

}
