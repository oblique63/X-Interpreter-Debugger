package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Virtual Machine used for executing a given program in Debug mode
 * @author Enrique Gavidia
 */
public class DebugVM extends VirtualMachine {
    private Stack<FunctionEnvironmentRecord> environmentStack;
    private List<SourceLineEntry> sourceCode;
    private ByteCode currentByteCode;
    private String stepMethod;
    private String readPrompt;
    private String traceString;
    private boolean trace;
    private boolean lineChanged;

    /**
     * Creates a new DebugVM instance
     * @param program The program object to be executed
     * @param sourceCode The original source code for the program
     */
    public DebugVM(Program program, List<SourceLineEntry> sourceCode) {
        super(program);
        this.isRunning = true;
        this.programCounter = 0;
        this.currentByteCode = null;
        this.readPrompt = "";
        this.runStack = new RunTimeStack();
        this.returnAddrs = new Stack<Integer>();
        this.environmentStack = new Stack<FunctionEnvironmentRecord>();

        // This inserts a 'dummy' function record to allow access to all the source
        // code prior to executing any byteCodes; this is done to prevent unnecessary
        // checking in the source-code methods themselves
        FunctionEnvironmentRecord main = new FunctionEnvironmentRecord();
        main.setName("main");
        main.setStartLine(1);
        main.setEndLine(sourceCode.size());
        main.setCurrentLine(1);
        this.environmentStack.add(main);

        this.sourceCode = sourceCode;
        this.lineChanged = false;
        this.trace = false;
    }

    @Override
    /**
     * Executes the program's bytecodes according to the specified stepping method
     */
    public void executeProgram() {
        int envStackSize = environmentStack.size();
        while (checkStepCondition(envStackSize) && isRunning) {

            currentByteCode = program.getCode(programCounter);
            if (currentByteCode.getName().matches("READ"))
                System.out.print(readPrompt);

            currentByteCode.execute(this);
            programCounter++;
        }

        // Once executing is done, reset the step method, and print the trace output
        // if tracing is on, and a non-intrinsic function was just entered/exited
        stepMethod = null;
        if (trace && environmentStack.size() != envStackSize && environmentStack.peek().getStartLine() > 0)
            System.out.println(traceString);
    }

    /**
     * Tells whether or not the program is still being executed
     * @return true - if program is still being executed; false - if program has finished executing
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Allows an external User Interface layer to set the display prompt
     * that's shown whenever the program requires user input
     * @param prompt The prompt to be displayed
     */
    public void setReadPrompt(String prompt) {
        readPrompt = prompt;
    }

    //----{ Stepping Methods }--------------------------------------------------
    /**
     * Used to set the method by which to step through the code; a step method
     * must be set before executing any code.
     * Accepts: "continue", "out","into","over"
     * @param step
     */
    public void setStepMethod(String step) {
        if (step.matches("continue|out|into|over"))
            stepMethod = step;
    }

    /**
     * Used to test when to end the main fetch-execute cycle based on the type
     * of step performed.
     * @param envStackSize The original size of the Environment Stack when the
     *                     fetch-execute cycle was entered.
     * @return A boolean value based on the step condition, dictating whether the
     *         fetch-execute cycle will continue or not.
     */
    private boolean checkStepCondition(int envStackSize) {
        boolean condition = false;
        if (stepMethod.matches("continue")) {
            condition = !isBreakPointSet(getCurrentLine()) || envStackSize == environmentStack.size() || !lineChanged;
            //System.out.println("CurrentLine: "+getCurrentLine()+"\n"+"lineChanged: "+lineChanged+"\n"+"Breakpoint? "+ isBreakPointSet(getCurrentLine()));
            if (isBreakPointSet(getCurrentLine()) && lineChanged)
                condition = false;

        } else if (stepMethod.matches("out")) {
            condition = environmentStack.size() >= envStackSize;
            if (isBreakPointSet(getCurrentLine()) && lineChanged)
                condition = false;

        } else if (stepMethod.matches("into")) {
            condition = environmentStack.size() <= envStackSize;
            // If stepping into a non-intrinsic function, allow for the FormalCode to be read in
            if (!condition && currentByteCode.getName().matches("FUNCTION") && environmentStack.peek().getStartLine() > 0)
                    condition = true;

        } else if (stepMethod.matches("over"))
            condition = !lineChanged;

        if (lineChanged)
            lineChanged = false;

        return condition;
    }

    //----{ SourceCode Methods }------------------------------------------------
    /**
     * Returns the number of the line currently being executed
     * @return The current line number
     */
    public int getCurrentLine() {
         return  environmentStack.peek().getCurrentLine();
    }

    /**
     * Sets the line number for which code is currently being executed for.
     * For use by ByteCodes.
     * @param lineNumber The current line number
     */
    public void setCurrentLine(int lineNumber) {
        // Only switch lines if it's to a possible source location, and if 
        // another funcion isn't about to be called
        if(lineNumber >= 0 && environmentStack.size() == runStack.frames() + 1) {
            // Update the current line of the top Function Environment Record
            FunctionEnvironmentRecord record = environmentStack.pop();
            record.setCurrentLine(lineNumber);
            environmentStack.add(record);
            lineChanged = true;
        }
    }

    /**
     * Gets the source code for the given line number
     * @param lineNumber The line whose code is is to be retrieved
     * @return The source code contained on the specified line number
     */
    public String getSourceLine(int lineNumber) {
        return sourceCode.get(lineNumber - 1).getSourceLine();
    }

    /**
     * Get the number of lines in the user's source code
     * @return  Total lines of code
     */
    public int getSourceSize() {
        return sourceCode.size();
    }

    //----{ BreakPoint Methods }------------------------------------------------
    /**
     * Checks whether a breakpoint is set at the given line number
     * @param line Line to be checked
     * @return A boolean describing whether a breakpoint is set or not
     */
    public boolean isBreakPointSet(int line) {
        if (line > 0)
            return sourceCode.get(line - 1).isBreakPointSet();
        else
            return false;
    }

    /**
     * Sets or Clears a BreakPoint at the given line
     * @param lineNumber The line on which to perform this operation
     * @param breakPoint Dictates whether to set (true) or clear (false) a breakpoint
     *                   at the given lineNumber
     * @return A boolean value describing whether the operation was successful or not.
     */
    public boolean setBreakPoint(int lineNumber, boolean breakPoint) {
        SourceLineEntry sourceLine = sourceCode.get(lineNumber);
        String line = sourceLine.getSourceLine();
        if (isValidBreakPoint(line)) {         
            sourceLine.setBreakPoint(breakPoint);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether a breakpoint can be set on the given line
     * @param line The line on which a breakpoint is to be set
     * @return A boolean describing whether or not the breakpoint is valid
     */
    private boolean isValidBreakPoint(String line) {
        return line.contains("int") || line.contains("boolean") ||
               line.contains("if")  || line.contains("while")   ||
               line.contains("=")   || line.contains("{")       ||
               line.contains("return");
    }

    //----{ Trace Methods }-----------------------------------------------------
    /**
     * Turns function tracing ON/OFF
     * @param showTrace true - ON; false - OFF
     */
    public void setTrace(boolean showTrace) {
        trace = showTrace;
        traceString = "";
    }

    /**
     * Called by FunctionRecord methods to log every time a function is entered/exited
     * @param isExit Tells whether a function is exiting, and adjusts the trace
     *               output accordingly
     */
    private void logTrace(boolean isExit) {
        String funcName = environmentStack.peek().getName().split("<<")[0];
        for (int space = 0; space < environmentStack.size(); space++)
            traceString += " ";

        if (isExit) {
            int returnValue = runStack.peek();
            traceString += "exit: " + funcName + ": " + returnValue + "\n";
        } else {
            String funcArgs = "";
            for (int index = runStack.peekFrame(); index < runStack.size(); index++) {
                funcArgs += runStack.elementAt(index);
                if (index != runStack.size()-1)
                    funcArgs += ",";
            }
            traceString += funcName + "(" + funcArgs + ")" + "\n";
        }
    }

    /**
     * Prints out the Call Stack
     */
    public void printCallStack() {
        String callStack = "";
        for (int index = environmentStack.size() - 1; index > 0; index--) {
            FunctionEnvironmentRecord func = environmentStack.elementAt(index);
            String funcName = func.getName().split("<<")[0];
            callStack += funcName + ": " + func.getCurrentLine() + "\n";
        }
        System.out.println(callStack);
    }

    //----{ Environment Stack Methods }-----------------------------------------
    /**
     * Adds a new function record to the Environment Stack;
     * to be used by ByteCodes when entering a new function.
     * @param name The name of the function
     * @param startLine The line at which the function starts
     * @param endLine The line at which the function ends
     * @see interpreter.debugger.FunctionEnvironmentRecord
     */
    public void addFunctionRecord(String name, int startLine, int endLine) {
        FunctionEnvironmentRecord record = new FunctionEnvironmentRecord();
        record.setName(name);
        record.setStartLine(startLine);
        record.setEndLine(endLine);
        record.setCurrentLine(getCurrentLine());
        environmentStack.add(record);

        // Don't trace if entering an intrinsic function
        if (trace && environmentStack.peek().getStartLine() > 0)
            logTrace(false);
    }

    /**
     * Removes the top function Environment record
     */
    public void popFunctionRecord() {
        // Don't trace if exiting an intrinsic function
        if (trace && environmentStack.peek().getStartLine() > 0)
            logTrace(true);

        environmentStack.pop();
    }

    //----{ Function Record Methods }-------------------------------------------
    /**
     * Adds an entry to the current Function Environment Record
     * @param id The ID of the entry being entered
     * @param offset The entry's offset in the runtime stack
     * @see interpreter.debugger.FunctionEnvironmentRecord#enter(java.lang.String, int)
     */
    public void addRecordEntry(String id, int offset) {
        FunctionEnvironmentRecord record = environmentStack.pop();
        record.enter(id, offset);
        environmentStack.add(record);
    }

    /**
     * Removes a specified number of entries in the current Function Environment Record
     * @param numberOfPops Number of entries to remove
     * @see interpreter.debugger.FunctionEnvironmentRecord#pop(int)
     */
    public void popRecordEntries(int numberOfPops) {
        FunctionEnvironmentRecord record = environmentStack.pop();
        record.pop(numberOfPops);
        environmentStack.add(record);
    }

    /**
     * Returns the line number at which the current function starts
     * @return Line number
     */
    public int getFirstFunctionLine() {
        return environmentStack.peek().getStartLine();
    }

    /**
     * Returns the line number at which the current function ends
     * @return Line number
     */
    public int getLastFunctionLine() {
        return environmentStack.peek().getEndLine();
    }

    /**
     * Returns the name of the current function
     * @return Funciton name
     */
    public String getCurrentFunctionName() {
        return environmentStack.peek().getName().split("<<")[0];
    }

    /**
     * Returns a list of the declared variables in the current function
     * @return List of variable IDs
     * @see interpreter.debugger.FunctionEnvironmentRecord#getVariables()
     */
    public Set<String> getFunctionVariables() {
        return environmentStack.peek().getVariables();
    }

    /**
     * Gets the value for a given variable name in the current function
     * @param var Variable ID
     * @return Variable's value
     */
    public int getVariableValue(String var) {
        int offset = environmentStack.peek().getVariableOffset(var);
        return runStack.elementAt(offset);
    }
}
