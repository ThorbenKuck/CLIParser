# CLIParser 

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.thorbenkuck/CLIParser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.thorbenkuck/CLIParser)

### What is this Framework?

This Frameworks is an abstraction of a unix-like CommandLineInterface (CLI). This framework is unique in the way, that it is full abstracted and can be used for different Tasks, than reading in CommandLine inputs.

### For Whom this is

This framework is for anyone, who needs a highly modular CLI, which can be called from anywhere, not just the CommandLine

### Installation

Put this into your pom.xml (if you are using Maven)

```
<dependency>
  <groupId>com.github.thorbenkuck</groupId>
  <artifactId>CLIParser</artifactId>
  <version>1.0</version>
</dependency>
```

Put this into your build.gradle (if you are using Gradle)

```
dependencies {
    compile group: 'com.github.thorbenkuck', name: 'CLIParser', version: '1.0'
}
```

### Setup

Setting anything else up, other than your project to realize the framework, is not required.

To setup any CLIParser inside your code, you can take various approache. Let's focus on the first attempt.

To create a CLIParser, coppy the following Snippet into your project:

```java
// Creating the default Printer, printing to System.out
Printer printer = Printer.getDefault();
// Creating the default CliParser, the StrategyBasedCliParser
CliParser cliParser = CliParser.getDefault(printer);
// Reading from the CommandLine, using the Printer, to print and cliParser to parse input
InputReader inputReader = InputReader.commandLine(printer, cliParser);

// This Command stopps the given InputReader, you can call it with "stop"
cliParser.addCommand(Command.defaultStopCommand(inputReader));
// This Command lists all registered Commands, you can call it with "help"
cliParser.addCommand(Command.defaultListCommand());
// Tell the cliParser to include the lastly executed Command with !!
cliParser.addPreParser(PreParser.bangbang());

try {
  inputReader.start();
} catch (IOException | InterruptedException e) {
  e.printStackTrace();
  System.exit(1);
}
```

Than start the application and you will see a little text appearing inside your console, prompting you with:    
#-----------------------------#    
#Started the CommandLineInputReader#    
#-----------------------------#    
This means, the CommandLineInputReader ist listening to your inputs. Dont worry, you can change the text.

### Working with this CLIParser

This CLIParser consists of 4 major parts:

1. The CliParser interface
2. The InputReader interface
3. The Printer interface
3. The Command interface

Explanation:

1. The CliParser interface defines what the parser can do. The major requriement is the methode <code>Boolean parse(String);</code>, which inherits from the Parser interface and (in the implementation of this interface) will take a string and call all required Commands. So, if you input <code>test -t 10 -h</code>, it will try to call the first Command with the identifer "test" and give them the options "t, 10" and "h", as an instance of the <code>Option</code> class. If you want to define your own Parser, you can use the AbstraceCliParser. If you want to go with a strategy-pattern-approach, take a look at the AbstractStrategyCliParser, with the twist, that you just have to define the Strategy it is using.
2. The InputReader interface provides methods to get stuff from anywhere. The method <code>viod start() throws IOException, InterruptedException;</code> tells the implementation to listen for inputs. The manner is defined by the implementation. Take the Class <code>CommandLineReader</code> for example. It listens to System.in for input. At the opposite, the <code>BlockingQueueInputReader</code> is waiting for a new element inside a <code>java.util.concurrent.BlockingQueue</code>. Both are coupled to the CliParser interface, to parse the accepted Line. If you consider writing your own InputReader, take a look at the AbstractInputReader class.
3. The Printer interface provides a simple connection to output something. By default, the <code>DefaultPrinter</code> is printing anything to System.out. But you can do anyhting, if you want to.
4. The Command interface defines what happens at which entered text. It mainly consists of an identifyer (for example sudo, in the world of unix) and a description about what this command does.

### The first Example indepth

Lets take a look at the first example again. We can safly ignore the firstline, because the printer is the least interresting part. Looking at the following snipped however is a bit more interresting:

```java
CliParser cliParser = CliParser.getDefault(printer);
```
This returns a new Instance of the <code>StrategyBasedCliParser</code>. Calling it this way, hides all the interresting stuff, for example the strategy itself, but lets take a lookt a it:

If we create a StrategyBasedCliParser, without providing a <code>ParsingStrategy</code> it uses
```java
ParsingStrategy.getDefault()
```
What does this mean? ParsingStrategy is an interface, which simply provides 3 methods. 
- hasMoreOptions(StringBuilder enteredText);
- String getCommand(StringBuilder enteredText);
- Option getNextOption(StringBuilder enteredText);

The Strategy itself only decouples how to Parse the text for the command identifier and the given parameters, or options if you want (well, i want). It gets a StringBuilder into the costructor, because a String is immutable and we want you to take care of ripping this entered text appart. If you are wondering at this point, the default ParsingStrategy is: <code>ModifyingSplittingParsingStrategy();</code>. It splitts the given String at " " and checks at maximum the first 2 entrys of the resulting array.

If you wanted to change the type of text-structur for your commands, you could write your own ParsingStrategy. At the default ParsingStrategy you cannot provide a String like: "-command option1 parameter1 option2", however, if you wanted to, you could implement this, writing your own strategy.

If you are feeling like this approach is a verry inefficient approach, you could write your own implementation and make it more efficient.

However, back to the roots! Think about it this way. The AbstractStrategyCliParser will do the following:
```java
StringBuilder stringBuilder = new StringBuilder(enteredText);
// get the command identifier
String command = parsingStrategy.getCommand(stringBuilder);
// Parse (or let parse) all Options:
List<Option> options = parseAllOptions(stringBuilder);
// Find any Command (if present) and run all
getCommands().stream()
        // for safe mesurments
				.filter(Objects::nonNull)
				.filter(command1 -> command.equals(command1.getIdentifier()))
				.forEachOrdered(command1 -> {
					command1.run(options, this);
				});
```
and parseAllOptions looks something like this:
```java
while (parsingStrategy.hasMoreOptions(stringBuilder)) {
  options.add(parsingStrategy.getNextOption(stringBuilder));
}
```
Afterwards, the setLastCommand method of the AbstractCliParser is called and the lastly (successfully of failed) parsed string is safed.

You can change this, if you wanted to, but you can also "live with this monstrousity".
