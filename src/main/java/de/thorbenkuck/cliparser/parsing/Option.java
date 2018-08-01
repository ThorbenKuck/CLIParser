package de.thorbenkuck.cliparser.parsing;

import java.io.Serializable;
import java.util.Objects;

/**
 * Eine Option ist ein Identifier - Parameter paar.
 * Traditionell fängt es mit einem "-" an. Also z.B.:
 * "-<i>identifier</i> <i>parameter</i>"
 * Der Parameter kann dabei auch leer sein.
 */
public class Option implements Serializable {

	private static final long serialVersionUID = 3004602455930473346L;
	private String optionIdentifier;
	private String parameter;

	/**
	 * Erstelle eine Neue Option.
	 * Dafür wird zum einen der Identifier und der Parameter erfordert.
	 * Damit nicht über Refferenzen eine Option später manipuliert werden kann, wird hier kein Setter für die
	 * entsprechenden Parameter des Objects angeboten.
	 *
	 * @param optionIdentifier der Identifier dieser Option
	 * @param parameter        der Parameter dieser Option
	 */
	public Option(String optionIdentifier, String parameter) {
		Objects.requireNonNull(optionIdentifier);
		Objects.requireNonNull(parameter);
		this.optionIdentifier = optionIdentifier;
		this.parameter = parameter;
	}

	private Option() {
	}

	@Override
	public String toString() {
		return optionIdentifier + (parameter.length() > 0 ? " " + parameter : "");
	}

	/**
	 * Der Identifier ist zur Unterscheidung unterschiedlicher Optionen da.
	 *
	 * @return den Identifier dieser Instanz.
	 */
	public String getOptionIdentifier() {
		if(optionIdentifier == null) {
			throw new IllegalStateException("Empty Option");
		}
		return optionIdentifier;
	}

	/**
	 * Ein Parameter ist als zusätzliche Information für die Identifier unabhängig von Identifier und kann, muss aber
	 * nicht mit angegeben werden.
	 *
	 * @return den Parameter dieser Option
	 */
	public String getParameter() {
		if(optionIdentifier == null) {
			throw new IllegalStateException("Empty Option");
		}
		return parameter;
	}

	public boolean isEmpty() {
		return isEmpty(this);
	}

	public static Option empty() {
		return new Option();
	}

	public static boolean isEmpty(Option option) {
		return option.getOptionIdentifier() == null;
	}
}
