package interpreter.debugger;

import java.util.Set;

/**
 * Used to store line and variable information for functions
 * @author Enrique Gavidia
 */
public class FunctionEnvironmentRecord {
    private DebugSymbolTable table;
    private int startLine, endLine, currentLine;
    private String name;

    /**
     * Creates a new function record; used when a new function is entered.
     */
    public FunctionEnvironmentRecord() {
        table = new DebugSymbolTable();
        table.beginScope();
    }

    /**
     * Enters a variable entry into the function record
     * @param id The variable's ID
     * @param offset The offset of the variable in the runtime stack
     */
    public void enter(String id, int offset) {
        table.put(id, offset);
    }

    /**
     * Sets the line at which the function starts
     * @param line Line number
     */
    public void setStartLine(int line) {
        startLine = line;
    }

    /**
     * Sets the line at which the function ends
     * @param line Line number
     */
    public void setEndLine(int line) {
        endLine = line;
    }

    /**
     * Sets which line is currently being executed in the function
     * @param line Current line number
     */
    public void setCurrentLine(int line) {
        currentLine = line;
    }

    /**
     * Sets the name of the function
     * @param name Function name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Pops the given number of entries from the record
     * @param numOfPops Number of entries to remove
     */
    public void pop(int numOfPops) {
        table.popValues(numOfPops);
    }


    /**
     * Returns the name of the function
     * @return Function name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the line number at which the function starts
     * @return Start line number
     */
    public int getStartLine() {
        return startLine;
    }

    /**
     * Returns the line number at which the function ends
     * @return End line number
     */
    public int getEndLine() {
        return endLine;
    }

    /**
     * Returns the line that is currently being executed in the function
     * @return Current line number
     */
    public int getCurrentLine() {
        return currentLine;
    }

    /**
     * Returns a set of the variable IDs currently stored in the function record
     * @return Set of variable IDs
     * @see interpreter.debugger.DebugSymbolTable#keys()
     */
    public Set<String> getVariables() {
        return table.keys();
    }

    /**
     * Returns the offset (in the runtime stack) at which the given variable is stored
     * @param var The variable ID whose offset is requested
     * @return The offset of the given variable ID
     */
    public int getVariableOffset(String var) {
        return table.get(var);
    }
}
