package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;

/**
 * Augments LitCode to add an entry to the VirtualMachine's current function record
 * containing the variable's offset
 * @author Enrique Gavidia
 * @see interpreter.bytecodes.LitCode
 */
public class LitCode extends interpreter.bytecodes.LitCode {
    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }

    public void execute(DebugVM vm) {
        super.execute(vm);
        
        // only add it to the function record if it is an actual declared variable
        if (!id.isEmpty()) {
            int offset = vm.runStackSize() - 1;
            vm.addRecordEntry(id, offset);
        }
    }
}
