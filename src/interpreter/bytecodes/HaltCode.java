package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Stops the Virtual Machine
 * @author Enrique Gavidia
 */
public class HaltCode extends ByteCode {
    public HaltCode(){}

    @Override
    public void init(String args) {}

    @Override
    public void execute(VirtualMachine vm) {
        vm.stopRunning();
    }

    @Override
    public String getArgs() {
        return "";
    }
}
