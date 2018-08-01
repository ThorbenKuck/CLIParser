package example;

import de.thorbenkuck.cliparser.CommandLineInputReader;
import de.thorbenkuck.cliparser.InputReader;
import de.thorbenkuck.cliparser.Printer;
import de.thorbenkuck.cliparser.parsing.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

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
		cliParser.addCommand(new RandomCommand());

		cliParser.addPreParser(PreParser.bangBang());

		inputReader.start();
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

		inputReader.start();
	}

	private final class RandomCommand extends AbstractCommand {

		private int bound = 100;

		protected RandomCommand() {
			super("random", "Prints a random Number");
			addOption("o", (s -> bound = Integer.parseInt(s)));
		}

		@Override
		protected void handle(String[] arguments, CliParser parser) {
			parser.print("" + ThreadLocalRandom.current().nextInt(bound));
		}
	}

}
