package example;

import de.thorbenkuck.cliparser.CommandLineInputReader;
import de.thorbenkuck.cliparser.InputReader;
import de.thorbenkuck.cliparser.Printer;
import de.thorbenkuck.cliparser.parsing.BlockingQueueInputReader;
import de.thorbenkuck.cliparser.parsing.CliParser;
import de.thorbenkuck.cliparser.parsing.Command;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.TestCase.assertTrue;

public class CompleteExample {

	private Printer printer = Printer.getDefault();
	private CliParser cliParser = CliParser.getDefault(printer);

	public static void main(String[] args) {
		new CompleteExample().testBlockingQueue();
	}

	public void test() {

		InputReader inputReader = new CommandLineInputReader(printer, cliParser);
		cliParser.addCommand(Command.defaultStopCommand(inputReader));
		cliParser.addCommand(Command.defaultListCommand());

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
