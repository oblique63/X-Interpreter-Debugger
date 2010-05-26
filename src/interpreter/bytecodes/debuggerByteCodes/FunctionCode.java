package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVM;

/**
 * Signals the start of a function, and declares its starting and ending lines
 * @author Enrique Gavidia
 */
public class FunctionCode extends ByteCode {
    private String funcName;
    private int startLine, endLine;

    @Override
    public void init(String args) {
        funcName = args.split(" ")[0];
        startLine = Integer.parseInt(args.split(" ")[1]);
        endLine = Integer.parseInt(args.split(" ")[2]);
    }

    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }

    public void execute(DebugVM vm) {
        vm.addFunctionRecord(funcName, startLine, endLine);
        vm.setCurrentLine(startLine);
    }

    @Override
    public String getArgs() {
        return funcName + " " + startLine + " " + endLine;
    }
}