package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVM;

/**
 * Signals the start of a new line in the original source code
 * @author Enrique Gavidia
 */
public class LineCode extends ByteCode {
    private int lineNumber;

    @Override
    public void init(String args) {
        lineNumber = Integer.parseInt(args);
    }

    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }

    public void execute(DebugVM vm) {
        vm.setCurrentLine(lineNumber);
    }

    @Override
    public String getArgs() {
        return "" + lineNumber;
    }

}
