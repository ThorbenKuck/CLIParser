package de.thorbenkuck.cliparser;

import de.thorbenkuck.cliparser.parsing.ModifyingSplittingParsingStrategy;
import de.thorbenkuck.cliparser.parsing.Option;
import de.thorbenkuck.cliparser.parsing.ParsingStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplittingTest {

	public static void main(String[] args) {
		ParsingStrategy strategy = new ModifyingSplittingParsingStrategy();

		StringBuilder stringBuilder = new StringBuilder("test -h 10 --super toll testing broadcast blablabla");
		List<Option> options = new ArrayList<>();

		String command = strategy.getCommand(stringBuilder);
		while (strategy.hasMoreOptions(stringBuilder)) {
			options.add(strategy.getNextOption(stringBuilder));
		}

		System.out.println(command);
		System.out.println(options);
		System.out.println(Arrays.toString(strategy.getArguments(stringBuilder)));
	}

}
