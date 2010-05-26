package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Labels a section of code to be executed
 * @author Enrique Gavidia
 */
public class LabelCode extends ByteCode{
    private String label;
    public LabelCode(){}

    @Override
    public void init(String args) {
        label = args;
    }

    @Override
    public void execute(VirtualMachine vm) {}

    @Override
    public String getArgs() {
        return label;
    }
}
