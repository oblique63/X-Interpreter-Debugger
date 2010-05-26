package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Toggles DUMP mode on the Virtual Machine
 * @author Enrique Gavidia
 */
public class DumpCode extends ByteCode {
    private Boolean dump;
    public DumpCode() {}

    @Override
    public void init(String args) {
        if (args.toUpperCase().matches("ON"))
            dump = true;
        else if (args.toUpperCase().matches("OFF"))
            dump = false;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.dumpRunStack(dump);
    }

    @Override
    public String getArgs() {
        throw new UnsupportedOperationException("getArgs method not supported by DumpCode.");
    }
}
