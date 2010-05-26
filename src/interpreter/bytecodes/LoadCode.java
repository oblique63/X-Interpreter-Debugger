package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Loads a value on to the Runtime stack
 * @see interpreter.RunTimeStack#load(int) 
 * @author Enrique Gavidia
 */
public class LoadCode extends ByteCode {
    private int offset;
    private String id;
    public LoadCode(){}

    @Override
    public void init(String args) {
        String argList[] = args.split("\\s");
        offset = Integer.parseInt(argList[0]);
        id = argList[1];
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.loadRunStack(offset);
    }

    @Override
    public String getArgs() {
        return Integer.toString(offset)+" "+id;
    }
}
