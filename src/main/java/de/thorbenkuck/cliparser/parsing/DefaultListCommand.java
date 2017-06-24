package de.thorbenkuck.cliparser.parsing;

public class DefaultListCommand extends AbstractCommand {
	public DefaultListCommand(String identifier) {
		super(identifier, (option, parser) -> {
				parser.print("\n#-#LIST#-#\nAll registered Commands:");
				for (Command c : parser.getCommands()) {
					parser.print(c.getIdentifier() + (! c.getDescription().equals("") ? " \"" + c.getDescription() + "\"" : "NO DESCRIPTION"));
				}
				parser.print("");
 				}, "Lists all Options, currently registered in the CliParser.");
	}
}
