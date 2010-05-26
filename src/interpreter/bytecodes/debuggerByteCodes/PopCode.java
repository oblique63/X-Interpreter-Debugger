package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;

/**
 * Augments PopCode to also remove entries from the VM's current Function Record
 * @author Enrique Gavidia
 * @see interpreter.bytecodes.PopCode
 */
public class PopCode extends interpreter.bytecodes.PopCode {
    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }

    public void execute(DebugVM vm) {
        super.execute(vm);
        vm.popRecordEntries(numOfPops);
    }
}
