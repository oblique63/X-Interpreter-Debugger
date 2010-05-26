package interpreter.bytecodes;
import interpreter.VirtualMachine;


/**
 * Abstract class for all the ByteCodes to be interpreted
 * @author Enrique Gavidia
 */
public abstract class ByteCode {

    /**
     * Initiates the ByteCode object
     * @param args Arguments to pass to the ByteCode
     */
    public abstract void init(String args);

    /**
     * Executes the ByteCode object's instructions
     * @param vm The Virtual Machine instance which is executing the ByteCode
     */
    public abstract void execute(VirtualMachine vm);

    /**
     * Gets the arguments passed to the ByteCode object when it was initiated
     * @return ByteCode's arguments
     */
    public abstract String getArgs();

    /**
     * Gets the name of the actual ByteCode the object represents
     * @return Name of the ByteCode
     */
    public String getName() {
        String codeName = this.getClass().getName().replaceFirst("interpreter.bytecodes.", "").replaceAll("Code", "");
        if (codeName.startsWith("debugger"))
            codeName = codeName.replaceFirst("debuggerBytes.", "");
        return codeName.toUpperCase();
    }
}