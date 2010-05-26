package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Calls a funtion
 * @author Enrique Gavidia
 */
public class CallCode extends ByteCode {
    private String funcName;
    public CallCode(){}

    @Override
    public void init(String args) {
        funcName = args;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int returnAddress = vm.getProgramCounter();
        vm.pushReturnAddrs(returnAddress);

        int goToAddress = Integer.parseInt(funcName.split(" ")[1]);
        vm.setProgramCounter(goToAddress-1);
    }

    @Override
    public String getArgs() {
        return funcName;
    }

}
