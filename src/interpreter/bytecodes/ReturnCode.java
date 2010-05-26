package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Returns from a function
 * @author Enrique Gavidia
 */
public class ReturnCode extends ByteCode {
    private String funcName;
    public ReturnCode(){}

    @Override
    public void init(String args) {
        funcName = args;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.popRunStackFrame();
        vm.setProgramCounter(vm.popReturnAddrs());
    }

    @Override
    public String getArgs() {
        return funcName;
    }
}
