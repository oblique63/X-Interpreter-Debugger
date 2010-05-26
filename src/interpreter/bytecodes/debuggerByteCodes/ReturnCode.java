package interpreter.bytecodes.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;

/**
 * Augments ReturnCode to also pop the top Function Record from the VM's
 * Environment Stack
 * @author Enrique Gavidia
 * @see interpreter.bytecodes.ReturnCode
 */
public class ReturnCode extends interpreter.bytecodes.ReturnCode {
    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        vm.popFunctionRecord();
    }
}
