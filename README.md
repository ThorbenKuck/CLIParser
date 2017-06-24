# CLIParser

### What is this Framework?

This Frameworks is an abstraction of a unix-like CommandLineInterface (CLI). This framework is unique in the way, that it is full abstracted and can be used for different Tasks, than reading in CommandLine inputs.

### For Whom this is

This framework is for anyone, who needs a highly modular CLI, which can be called from anywhere, not just the CommandLine

### Installation

I hopefully will bring up maven, so it will be easyer, but at this time, i simply dont know how.

At the moment you have to:
- clone this project
- run the gradle wrapper
- create a jar file
- include the jar file into your project

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

Than start the application and you will see a little text, prompting you with:    
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

1. The CliParser interface defines what the parser can do. The major requriement is the methode <code>Boolean parse(String);</code>, which inherits from the Parser interface. If you want to define your own Parser, you can use the AbstraceCliParser. If you want to go with a strategy-pattern-approach, take a look at the AbstractStrategyCliParser, with the twist, that you just have to define the Strategy it is using.
2. The InputReader interface provides methods to get stuff from anywhere. The method <code>viod start() throws IOException, InterruptedException;</code> tells the implementation to listen for inputs. The manner is defined by the implementation. Take the Class <code>CommandLineReader</code> for example. It listens to System.in for input. At the opposite, the <code>BlockingQueueInputReader</code> is waiting for a new element inside a <code>java.util.concurrent.BlockingQueue</code>. Both are coupled to the CliParser interface, to parse the accepted Line. If you consider writing your own InputReader, take a look at the AbstractInputReader class.
3. The Printer interface provides a simple connection to output something. By default, the <code>DefaultPrinter</code> is printing anything to System.out. But you can do anyhting, if you want to.
4. The Command interface defines what happens at which entered text. It mainly consists of an identifyer (for example sudo, in the world of unix) and a description about what this command does.

### 
