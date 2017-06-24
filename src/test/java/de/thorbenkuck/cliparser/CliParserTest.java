package de.thorbenkuck.cliparser;

import de.thorbenkuck.cliparser.parsing.CliParser;
import de.thorbenkuck.cliparser.parsing.DefaultCommand;
import de.thorbenkuck.cliparser.parsing.Option;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CliParserTest {

	private int count = 0;
	private CliParser cliParser;
	private String enteredText;

	@Before
	public void before() {
		// Arrange
		cliParser = CliParser.getDefault(Printer.getDefault());
		count = 0;
	}

	@Test
	public void testDefaultStrategyParsing() {
		// Arrange
		enteredText = "test -higher 10 -higher 20 -lower 5";

		// Act
		cliParser.addCommand(new DefaultCommand("test", (options, parser) -> {
			for(Option option : options) {
				if(option.getOptionIdentifier().equals("higher")) {
					count += Integer.parseInt(option.getParameter());
				} else if(option.getOptionIdentifier().equals("lower")) {
					count -= Integer.parseInt(option.getParameter());
				}
			}
		}));
		cliParser.parse(enteredText);

		// Assert
		assertEquals(25, count);
	}

	@Test
	public void testComplicatedText() {
		// Arrange
		enteredText = "test -higher 10 -h -l -higher 20 -higher 10 -lower 10 -l -lower 5";

		// Act
		cliParser.addCommand(new DefaultCommand("test", (options, parser) -> {
			for(Option option : options) {
				if(option.getOptionIdentifier().equals("h")) {
					count += 5;
				} else if(option.getOptionIdentifier().equals("higher")) {
					count += Integer.parseInt(option.getParameter());
				} else if(option.getOptionIdentifier().equals("l")) {
					count -= 5;
				} else if(option.getOptionIdentifier().equals("lower")) {
					count -= Integer.parseInt(option.getParameter());
				} else {
					parser.printError("Unknown Option: " + option);
				}
			}
		}));
		cliParser.parse(enteredText);

		// Assert
		assertEquals(20, count);
	}
}