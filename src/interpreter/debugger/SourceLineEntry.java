package interpreter.debugger;

/**
 * Stores a line of source code, along with a boolean value dictating whether
 * a breakpoint is set at the line or not.
 * @author Enrique Gavidia
 */
public class SourceLineEntry {
    private String sourceLine;
    private boolean isBreakPointSet;

    /**
     * Creates a SourceLineEntry object, and stores the given line of source code
     * @param sourceLine The line of code to be stored
     * @param isBreakPointSet Signals whether a breakpoint is set at the line or not
     */
    public SourceLineEntry(String sourceLine, boolean isBreakPointSet) {
        this.isBreakPointSet = isBreakPointSet;
        this.sourceLine = sourceLine;
    }

    /**
     * Returns the line of source code that is stored in the object
     * @return Source line
     */
    public String getSourceLine() {
        return sourceLine;
    }

    /**
     * Tells whether or not a breakpoint is set at the line stored in the object
     * @return true - breakpoint is set; false - no breakpoint is set
     */
    public boolean isBreakPointSet() {
        return isBreakPointSet;
    }

    /**
     * Sets or clears a breakpoint on the stored line of code
     * @param breakPoint true - set breakpoint; false - clear breakpoint
     */
    public void setBreakPoint(boolean breakPoint) {
        isBreakPointSet = breakPoint;
    }
}
