package de.thorbenkuck.cliparser.parsing;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OptionConsumerWrapper implements BiConsumer<List<Option>, CliParser> {

	private Consumer<List<Option>> consumer;

	public OptionConsumerWrapper(Consumer<List<Option>> consumer) {
		Objects.requireNonNull(consumer);
		this.consumer = consumer;
	}

	@Override
	public final void accept(List<Option> option, CliParser cliParser) {
		consumer.accept(option);
	}

	@Override
	public final String toString() {
		return consumer.toString();
	}

	@Override
	public final boolean equals(Object o) {
		return consumer.equals(o);
	}

	@Override
	public final int hashCode() {
		return consumer.hashCode();
	}
}
