package de.thorbenkuck.cliparser.parsing;

import de.thorbenkuck.cliparser.AbstractInputReader;
import de.thorbenkuck.cliparser.Printer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueInputReader extends AbstractInputReader {

	private final BlockingQueue<String> blockingQueue;
	private CliParser cliParser;

	public BlockingQueueInputReader() {
		this(Printer.getDefault());
	}

	public BlockingQueueInputReader(Printer printer) {
		this(printer, CliParser.getDefault(printer));
	}

	public BlockingQueueInputReader(CliParser cliParser) {
		this(Printer.getDefault(), cliParser);
	}

	public BlockingQueueInputReader(BlockingQueue<String> blockingQueue) {
		this(Printer.getDefault(), blockingQueue);
	}

	public BlockingQueueInputReader(Printer printer, CliParser cliParser) {
		this(printer, cliParser, new LinkedBlockingQueue<>());
	}

	public BlockingQueueInputReader(Printer printer, BlockingQueue<String> blockingQueue) {
		this(printer, CliParser.getDefault(printer), blockingQueue);
	}

	public BlockingQueueInputReader(CliParser cliParser, BlockingQueue<String> blockingQueue) {
		this(Printer.getDefault(), cliParser, blockingQueue);
	}

	public BlockingQueueInputReader(Printer printer, CliParser cliParser, BlockingQueue<String> blockingQueue) {
		super(printer);
		this.cliParser = cliParser;
		this.blockingQueue = blockingQueue;
	}

	@Override
	protected void executeCommand(String line) {
		cliParser.parse(line);
	}

	@Override
	protected String getNextString() throws InterruptedException, IOException {
		String string = blockingQueue.take();
		printMessage(string + "\n", false);
		return string;
	}
}
