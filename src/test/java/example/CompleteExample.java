package example;

import de.thorbenkuck.cliparser.CommandLineInputReader;
import de.thorbenkuck.cliparser.InputReader;
import de.thorbenkuck.cliparser.Printer;
import de.thorbenkuck.cliparser.parsing.*;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.TestCase.assertTrue;

public class CompleteExample {

	private Printer printer = Printer.getDefault();
	private CliParser cliParser = CliParser.getDefault(printer);

	public static void main(String[] args) {
		new CompleteExample().test();
	}

	public void test() {
		InputReader inputReader = new CommandLineInputReader(printer, cliParser);
		cliParser.addCommand(Command.defaultStopCommand(inputReader));
		cliParser.addCommand(Command.defaultListCommand());
		cliParser.addCommand(new DefaultCommand("random"
				, (options, parser) -> {
					int bound = 100;
					for(Option option : options) {
						if (option.getOptionIdentifier().equals("o")) {
							bound = Integer.parseInt(option.getParameter());
						}
					}
					parser.print("" + ThreadLocalRandom.current().nextInt(bound));
				}, "Prints a random number"));

		cliParser.addPreParser(PreParser.bangBang());

		try {
			inputReader.start();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testBlockingQueue() {
		BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
		InputReader inputReader = new BlockingQueueInputReader(printer, cliParser, blockingQueue);
		cliParser.addCommand(Command.defaultStopCommand(inputReader));
		cliParser.addCommand(Command.defaultListCommand());

		new Thread(() -> {
			try {
				Thread.sleep(100);
				blockingQueue.add("help");
				blockingQueue.add("stop");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		try {
			inputReader.start();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
