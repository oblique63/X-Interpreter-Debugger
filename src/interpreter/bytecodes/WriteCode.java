package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Writes out the last value on the Runtime stack
 * @author Enrique Gavidia
 */
public class WriteCode extends ByteCode {
    public WriteCode(){}

    @Override
    public void init(String args){}

    @Override
    public void execute(VirtualMachine vm) {
        System.out.println(vm.peekRunStack());
    }

    @Override
    public String getArgs() {
        return "";
    }
}
