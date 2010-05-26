package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Removes the indicated top values from the Runtime stack
 * @author Enrique Gavidia
 */
public class PopCode extends ByteCode {
    protected int numOfPops;
    public PopCode() {}

    @Override
    public void init(String args) {
        numOfPops = Integer.parseInt(args);
    }

    @Override
    public void execute(VirtualMachine vm) {
        for (int i = 0; i < numOfPops; i++)
            vm.popRunStack();
    }

    @Override
    public String getArgs() {
        return Integer.toString(numOfPops);
    }

}
