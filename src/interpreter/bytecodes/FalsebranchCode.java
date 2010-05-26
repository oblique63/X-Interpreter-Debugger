package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Branches execution based on last computed value
 * @author Enrique Gavidia
 */
public class FalsebranchCode extends ByteCode{
    private String label;
    public FalsebranchCode(){}

    @Override
    public void init(String args) {
        label = args;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int boolValue = vm.popRunStack();
        if (boolValue == 0) {
            int goToAddress = Integer.parseInt(label.split(" ")[1]);
            vm.setProgramCounter(goToAddress);
        }
    }

    @Override
    public String getArgs() {
        return label;
    }
}
