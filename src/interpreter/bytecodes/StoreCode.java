package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Stores a value on to the Runtime stack
 * @see interpreter.RunTimeStack#store(int)
 * @author Enrique Gavidia
 */
public class StoreCode extends ByteCode {
    private int offset;
    private String id;
    public StoreCode(){}

    @Override
    public void init(String args) {
        String argList[] = args.split("\\s");
        offset = Integer.parseInt(argList[0]);
        id = argList[1];
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.storeRunStack(offset);
    }

    @Override
    public String getArgs() {
        return Integer.toString(offset)+" "+id;
    }
}
