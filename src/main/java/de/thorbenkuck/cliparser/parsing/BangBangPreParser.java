package de.thorbenkuck.cliparser.parsing;

class BangBangPreParser implements PreParser {

	private final String bangBang = "!!";
	private String lastString = "";

	@Override
	public void accept(StringBuilder stringBuilder, CliParser cliParser) {
		String string = stringBuilder.toString();
		if (string.contains(bangBang) && !lastString.equals(string)) {
			if (cliParser.getLastCommand().equals("")) {
				stringBuilder.replace(0, string.length(), "");
				return;
			}
			String replacement = string.replaceAll(bangBang, cliParser.getLastCommand());
			stringBuilder.delete(0, string.length()).append(replacement);
			accept(stringBuilder, cliParser);
		}
	}
}
