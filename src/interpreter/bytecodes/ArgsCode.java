package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Prepares the arguments to be passed to a funtion call
 * @author Enrique Gavidia
 */
public class ArgsCode extends ByteCode {
    private int numOfArgs;
    public ArgsCode(){}

    @Override
    public void init(String args) {
        numOfArgs = Integer.parseInt(args);
    }

    @Override
    public void execute(VirtualMachine vm) {
        int offset = vm.runStackSize() - numOfArgs;
        vm.newRunStackFrameAt(offset);
    }

    @Override
    public String getArgs() {
        return Integer.toString(numOfArgs);
    }
}
