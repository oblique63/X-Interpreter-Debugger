package interpreter.debugger.ui;

import interpreter.debugger.DebugVM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Commandline-based User Interface for the X-Debugger
 * @author Enrique Gavidia
 */
public class DebuggerUI {
    private static DebugVM vm;
    private static boolean exit;

    /**
     * To make DebuggerUI a singleton object, since it would not make sense to
     * heve multiple instances of it for a single program.
     */
    private DebuggerUI() {}

    /**
     * Displays the user interface for the debugger
     * @param virtuialMachine The DebugVM instance to be used for debugging
     */
    public static void displayInterface(DebugVM virtuialMachine) {
        exit = false;
        vm = virtuialMachine;
        String command = null;
        vm.setReadPrompt("Enter an integer: ");

        displayFunctionSource();
        System.out.println("X-Debugger: type '?' for a detailed list of commands.");

        while (!exit && vm.isRunning()) {
            try {
                System.out.print(">> ");
                BufferedReader input = new BufferedReader( new InputStreamReader( System.in ) );
                command = input.readLine().toLowerCase();
                executeCommand(command);
            } catch (IOException ex) {}
        }
        System.out.println("****Execution Halted: Exiting Debugger****");
    }


    /**
     * Determines what action the user wants performed based on the command given
     * @param command The user's command
     */
    private static void executeCommand(String command) {
        String arg = "";
        if (command.split(" ").length > 1)
            arg = command.split(" ",2)[1];

        command = command.split(" ")[0];

        if (command.startsWith("help")||command.startsWith("?"))
            help();
        else if (command.matches("c"))
            cont();
        else if (command.matches("over"))
            stepOver();
        else if (command.matches("out"))
            stepOut();
        else if (command.matches("in"))
            stepInto();
        else if (command.matches("brklst"))
            listBreakPoints();
        else if (command.matches("brk") && arg.matches("[\\d+\\s*]+"))
            setBreakPoint(arg);
        else if (command.matches("clr") && arg.matches("[\\d+\\s*]+"))
            clearBreakPoint(arg);
        else if (command.matches("src"))
            displayFunctionSource();
        else if (command.matches("vars"))
            displayVariables();
        else if (command.matches("trace") && arg.matches("on|off"))
            setTrace(arg);
        else if (command.matches("calls"))
            printCallStack();
        else if (command.startsWith("q"))
            quit();
        else
            System.out.println("Error: Invalid command; "
                + "type '?' to get a list of avalible commands.");
    }
    

    //----{ Command Implementations }-------------------------------------------
    private static void help() {
        String format = "%1$-14s %2$s \n";
        String output = ""
        + String.format(format,
            "COMMAND","DESCRIPTION")
        + String.format(format,
            "?", "Displays a detailed list of avalible commands")
        + String.format(format,
            "c", "Continues execution of the program until the next breakpoint")
        + String.format(format,
            "over", "Step over the current line")
        + String.format(format,
            "out", "Step out of the current function")
        + String.format(format,
            "in", "Step into the function on the current line")
        + String.format(format,
            "brk N", "Sets a breakpoint at the N-th line of the source code; accepts multiple line numbers")
        + String.format(format,
            "clr N", "Clears the breakpoint at the N-th line of the source code; accepts multiple line numbers")
        + String.format(format,
            "brklst", "Displays a list of the current breakpoint locations")
        + String.format(format,
            "src", "Displays the source code for the current function")
        + String.format(format,
            "vars", "Displays a list of the current variables in the program")
        + String.format(format,
            "trace ON/OFF", "Sets whether or not to trace function calls whenever a step/continue is executed")
        + String.format(format,
            "calls", "Prints the call stack")
        + String.format(format,
            "q", "Quits execution and exits the debugger");

        System.out.println(output);
    }

    private static void setBreakPoint(String lineNumbers) {
        String[] lines = lineNumbers.split(" ");
        String successOutput = "";
        for (String line : lines) {
            int lineNumber = Integer.parseInt(line);

            if (lineNumber <= vm.getSourceSize()) {
                boolean success = vm.setBreakPoint(lineNumber - 1, true);
                if (success)
                    successOutput += lineNumber + " ";
                else
                    System.out.println("Error: cannot set breakpoint on line " + lineNumber + ".");
            } else
                System.out.println("Error: line " + lineNumber + " does not exist.");
        }

        if (!successOutput.isEmpty())
            System.out.println("BreakPts set: " + successOutput);
    }

    private static void clearBreakPoint(String lineNumbers) {
        String[] lines = lineNumbers.split(" ");
        String successOutput = "";
        for (String line : lines) {
            int lineNumber = Integer.parseInt(line);

            if (lineNumber <= vm.getSourceSize()) {
                boolean success = vm.setBreakPoint(lineNumber - 1, false);
                if (success)
                    successOutput += lineNumber + " ";
            } else
                System.out.println("Error: line " + lineNumber + " does not exist.");
        }

        if (!successOutput.isEmpty())
            System.out.println("BreakPts cleared: " + successOutput);
    }

    private static void listBreakPoints() {
        String breakpoints = "";
        for (int line = 1; line <= vm.getSourceSize(); line++)
            if (vm.isBreakPointSet(line))
                breakpoints += line + " ";

        System.out.println("Current BreakPts: " + breakpoints);
    }

    private static void performStep(String stepMethod) {
        vm.setStepMethod(stepMethod);
        vm.executeProgram();
        displayFunctionSource();
    }

    private static void cont() {
        performStep("continue");
    }

    private static void stepOver() {
        performStep("over");
    }

    private static void stepOut() {
        performStep("out");
    }

    private static void stepInto() {
        performStep("into");
    }

    private static void displayFunctionSource() {
        int start = vm.getFirstFunctionLine();
        int end = vm.getLastFunctionLine();
        int current = vm.getCurrentLine();

        String format = "%1$4s %2$s";
        String output = "";

        if (start < 0)
            output = "****"+vm.getCurrentFunctionName().toUpperCase()+"****";
        else {
            for (int line = start; line <= end; line++) {
                if (vm.isBreakPointSet(line))
                    output += "*";
                else
                    output += " ";

                output += String.format(format, line + ".", vm.getSourceLine(line));

                if (line == current)
                    output += " <----";

                output += "\n";
            }
        }
        System.out.println(output);
    }

    private static void displayVariables() {
        String output = "";
        for (String var : vm.getFunctionVariables()) {
            output += var + ": " + vm.getVariableValue(var) + "\n";
        }
        System.out.println(output);
    }

    private static void setTrace(String arg) {
        if (arg.matches("on"))
            vm.setTrace(true);
        else
            vm.setTrace(false);
    }

    private static void printCallStack() {
        vm.printCallStack();
    }

    private static void quit() {
        vm.stopRunning();
        exit = true;
    }
}
