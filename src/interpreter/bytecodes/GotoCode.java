package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Moves exection to a specified location in the program
 * @author Enrique Gavidia
 */
public class GotoCode extends ByteCode {
    private String label;
    public GotoCode(){}

    @Override
    public void init(String args) {
        label = args;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int goToAddress = Integer.parseInt(label.split(" ")[1]);
        vm.setProgramCounter(goToAddress-1);
    }

    @Override
    public String getArgs() {
        return label;
    }
}
