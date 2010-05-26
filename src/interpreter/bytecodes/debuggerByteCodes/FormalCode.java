package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import interpreter.debugger.DebugVM;

/**
 * Declares a formal argument for a function, and its offset in the runtime stack
 * @author Enrique Gavidia
 */
public class FormalCode extends ByteCode {
    private String id;
    private int offset;

    @Override
    public void init(String args) {
        id = args.split(" ")[0];
        offset = Integer.parseInt(args.split(" ")[1]);
    }

    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }

    public void execute(DebugVM vm) {
        vm.addRecordEntry(id, offset + vm.runStackSize() - 1);
    }
    
    @Override
    public String getArgs() {
        return id + " " + offset;
    }

}