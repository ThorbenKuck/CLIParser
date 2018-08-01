package de.thorbenkuck.cliparser;

import de.thorbenkuck.cliparser.parsing.AbstractCommand;
import de.thorbenkuck.cliparser.parsing.CliParser;
import org.junit.Before;
import org.junit.Ignore;
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
		cliParser.addCommand(new TestCommand());
		cliParser.parse(enteredText);

		// Assert
		assertEquals(25, count);
	}

	@Test
	public void testComplicatedText() {
		// Arrange
		enteredText = "test -higher 10 -h -l -higher 20 -higher 10 -lower 10 -l -lower 5";

		// Act
		cliParser.addCommand(new TestCommand());
		cliParser.parse(enteredText);

		// Assert
		assertEquals(20, count);
	}

	@Ignore
	@Test
	public void integrationTest() {
		// Arrange
		enteredText = "test -higher 10 -h -l -higher 20 -higher 10 -lower 10 -l -lower 5 20 10 15";

		// Act
		cliParser.addCommand(new TestCommand());
		cliParser.parse(enteredText);

	}

	private class TestCommand extends AbstractCommand {

		protected TestCommand() {
			super("test");
			doNotEvaluateOptions();
			addOption("h", s -> count += 5);
			addOption("l", s -> count -= 5);
			addOption("higher", s -> count += Integer.parseInt(s));
			addOption("lower", s -> count -= Integer.parseInt(s));
		}

		@Override
		protected void handle(String[] arguments, CliParser parser) {
			if (arguments.length == 0) {
				evaluateOptions();
			}
			for (String string : arguments) {
				count = Integer.parseInt(string);
				evaluateOptions();
				parser.print("Count: " + count);
			}
		}
	}
}