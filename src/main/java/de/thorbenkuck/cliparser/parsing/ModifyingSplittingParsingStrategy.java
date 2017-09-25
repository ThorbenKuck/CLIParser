package de.thorbenkuck.cliparser.parsing;

public class ModifyingSplittingParsingStrategy implements ParsingStrategy {

	private void cleanup(StringBuilder stringBuilder) {
		if(stringBuilder.toString().startsWith(" ")) {
			stringBuilder.delete(0, 1);
			cleanup(stringBuilder);
		}
	}

	private String[] stringBuilderToStringArray(StringBuilder stringBuilder) {
		String currentString = stringBuilder.toString();
		return currentString.split(" ");
	}

	private Option getSingleOption(String identifier) {
		if(identifier.startsWith("-")) {
			return getSingleOption(identifier.substring(1));
		}
		return new Option(identifier, "");
	}

	private Option getParameterizeOption(String s1, String s2) {
		if(s1.startsWith("-")) {
			return getParameterizeOption(s1.substring(1), s2);
		} else {
			return new Option(s1, s2);
		}
	}

	private boolean isParameterLessOption(String s1, String s2) {
		return (s1.startsWith("-") && s2.startsWith("-"));
	}


	private Option getSingleOptionAndCleanUp(StringBuilder stringBuilder, String[] stringArray) {
		String string = stringArray[0];
		Option toReturn = getSingleOption(string);
		stringBuilder.delete(0, string.length());
		cleanup(stringBuilder);
		return toReturn;

	}

	private boolean isOption(String s) {
		return s.startsWith("-");
	}

	@Override
	public boolean hasMoreOptions(StringBuilder stringBuilder) {
		String[] stringParts = stringBuilderToStringArray(stringBuilder);
		for(String string : stringParts) {
			if(isOption(string)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getCommand(StringBuilder stringBuilder) {
		String[] stringArray = stringBuilderToStringArray(stringBuilder);
		if(stringArray.length > 0 && !isOption(stringArray[0])) {
			String toReturn = stringArray[0];
			stringBuilder.delete(0, toReturn.length());
			cleanup(stringBuilder);
			return toReturn;
		} else {
			return "";
		}
	}

	@Override
	public Option getNextOption(StringBuilder stringBuilder) {
		String[] stringArray = stringBuilderToStringArray(stringBuilder);
		if(stringArray.length == 0) {
			return Option.empty();
		}
		if(stringArray.length == 1 || isParameterLessOption(stringArray[0], stringArray[1])) {
			return getSingleOptionAndCleanUp(stringBuilder, stringArray);
		} else {
			String s1 = stringArray[0];
			String s2 = stringArray[1];
			Option toReturn = getParameterizeOption(s1, s2);
			stringBuilder.delete(0, stringArray[0].length());
			cleanup(stringBuilder);
			stringBuilder.delete(0, stringArray[1].length());
			cleanup(stringBuilder);
			return toReturn;
		}
	}
//	/**
//	 * Parsed das nächste OptionPair. Ein OptionPair ist dabei eine BareOption gefolgt von einem Parameter.
//	 * Dabei ist wichtig, dass die nächste Option eine PairOption ist, ansonsten kann es zu Problemen kommen.
//	 *
//	 * @return das nächste OptionPair
//	 */
//	private Option parseNextOptionPair() {
//		if (containsMoreOptionPairs(text)) {
//			String textToReturn1 = text.substring(0, text.contains(" ") ? text.indexOf(" ") : text.length());
//			textToReturn1 = textToReturn1.replace("-", "");
//			deleteNextStringPart();
//			String textToReturn2 = text.substring(0, text.contains(" -") ? text.indexOf(" -") : text.length());
//			deleteNextStringPart();
//			return new Option(textToReturn1, textToReturn2);
//		}
//		return new Option("", "");
//	}
//
//	/**
//	 * Eine BareOption ist ein Option, welche keinen hinterlegten Parameter hat.
//	 * Dabei ist wichtig, dass die nächste Option eine BareOption ist, ansonsten wir der Parameter des Option ignoriert.
//	 *
//	 * @return die nächste BareOption aus dem Text.
//	 */
//	private Option parseNextBareOption() {
//		if (containsMoreBareOptions(text)) {
//			String textToReturn = text.substring(0, text.contains(" ") ? text.indexOf(" ") : text.length());
//			textToReturn = textToReturn.replace("-", "");
//			deleteNextStringPart();
//			return new Option(textToReturn, "");
//		}
//		return new Option("", "");
//	}
//
//	/**
//	 * Löscht den nächsten Teil des Strings. Dabei wird der neu entstandene Text zurück gegeben.
//	 *
//	 * @return den neu entstandenen String.
//	 */
//	public String deleteNextStringPart() {
//		int start = text.contains(" ") ? text.indexOf(" ") + 1 : (text.contains(" -") ? text.indexOf(" -") + 1 : text.length());
//		if (text.length() >= start) {
//			text = text.substring(start);
//		}
//		return text;
//	}


}
