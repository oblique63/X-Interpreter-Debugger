package interpreter;

import interpreter.bytecodes.ByteCode;
import java.util.Stack;


/**
 * Virtual Machine for the X-language compiler
 * @author Enrique Gavidia
 */
public class VirtualMachine {
    protected Program program;
    protected int programCounter;
    protected RunTimeStack runStack;
    protected Stack<Integer> returnAddrs;
    protected Boolean isRunning;
    protected Boolean dump;

    /**
     * @param program Program object containing the ByteCodes the Virtual Machine
     *                will run.
     * @see interpreter.Program
     */
    public VirtualMachine(Program program) {
        this.program = program;
    }

    /**
     * Executes the program object the VM was instantiated with. Iterates through
     * the ByteCodes contained in the program object, executes their
     * instructions, and loads indicated values on to the Runtime stack.
     */
    public void executeProgram() {
        programCounter = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        dump = false;

        while (isRunning) {
            ByteCode code = program.getCode(programCounter);
            code.execute(this);
            
            if (dump)
                dump(code);

            programCounter++;
        }
    }

    /**
     * Displays dump information from the Runtime stack, and the given ByteCode
     * (including special dump instructions)
     * @param code ByteCode object used to obtain dump information
     * @see interpreter.RunTimeStack#dump()
     */
    private void dump(ByteCode code) {
        String codeName = code.getName();
        String output = "";
        if (!codeName.matches("DUMP")) {
            // Get the ByteCode's name and its arguments
            String[] argList = code.getArgs().split("\\s");
            output += codeName+" "+argList[0]+" ";

            // Check if the Code has additional special dump instructions
            if (codeName.matches("LIT|LOAD|STORE|RETURN|CALL")) {
                String spacing = "\t";
                if (codeName.matches("LIT")) {
                    // Get and format LIT id
                    if (argList.length > 1)
                        output += argList[1]+spacing+"int "+argList[1];
                } else if (codeName.matches("LOAD")) {
                    // Get formatted Load id
                    output += argList[1]+spacing+"<load "+argList[1]+">";
                } else if (codeName.matches("STORE")) {
                    // Display value of Stored id
                    output += argList[1]+spacing+argList[1]+"="+runStack.peek();

                } else if (codeName.matches("RETURN")) {
                    // Get id and return value of exiting function
                    String funcName = argList[0].split("<<")[0];
                    output += spacing+"exit "+funcName+": "+runStack.peek();

                } else if (codeName.matches("CALL")) {
                    // Get formatted funtion id with its arguments
                    String funcName = argList[0].split("<<")[0];
                    String funcArgs = "";
                    for (int i = runStack.peekFrame(); i < runStack.size(); i++) {
                        funcArgs += runStack.elementAt(i);
                        if (i != runStack.size()-1)
                            funcArgs += ",";
                    }
                    output += spacing+funcName+"("+funcArgs+")";
                }
            }
            
            // Print the ByteCode's dump output, along with a dump of the Runtime stack
            System.out.println(output);
            runStack.dump();
        }
    }

    //----{ For Use By ByteCodes }----------------------------------------------

    /**
     * Stops the Virtual Machine
     */
    public void stopRunning() {
        isRunning = false;
    }

    /**
     * Gets the Virtual Machine's current location in the program
     * @return Current location in the program
     */
    public int getProgramCounter() {
        return programCounter;
    }

    /**
     * Sets the Virtual Machine's current location
     * @param pc New location in the program
     */
    public void setProgramCounter(int pc) {
        programCounter = pc;
    }


    //----{ runStack methods }--------------------------------------------------

    /**
     * Get the value at the top of the Runtime stack
     * @return Top element of the Runtime stack
     * @see interpreter.RunTimeStack#peek()
     */
    public int peekRunStack() {
        return runStack.peek();
    }

    /**
     * Removes top element from the Runtime stack
     * @return The element just removed
     * @see interpreter.RunTimeStack#pop()
     */
    public int popRunStack() {
        return runStack.pop();
    }

    /**
     * Adds an int value to the Runtime stack
     * @param value  The value to be added
     * @return the value just added
     * @see interpreter.RunTimeStack#push(int)
     */
    public int pushRunStack(int value) {
        return runStack.push(value);
    }

    /**
     * Adds an Integer value to the Runtime stack
     * @param value  The value to be added
     * @return The value just added
     * @see interpreter.RunTimeStack#push(java.lang.Integer)
     */
    public Integer pushRunStack(Integer value) {
        return runStack.push(value);
    }

    /**
     * Pops the top of the Runtime stack, and stores the value at the given offset
     * @param offset Where to store the 'popped' value
     * @return The value just stored
     * @see interpreter.RunTimeStack#store(int)
     */
    public int storeRunStack(int offset) {
        return runStack.store(offset);
    }

    /**
     * Push the value at the Runtime stack's 'offset' index, to the top of the
     * Runtime stack
     * @param offset Index of the value to add to the Runtime stack
     * @return The value just added
     * @see interpreter.RunTimeStack#load(int)
     */
    public int loadRunStack(int offset) {
        return runStack.load(offset);
    }

    /**
     * Create a new Runtime stack frame at the given offset
     * @param offset Where to create the new frame
     * @see interpreter.RunTimeStack#newFrameAt(int)
     */
    public void newRunStackFrameAt(int offset) {
        runStack.newFrameAt(offset);
    }

    /**
     * Remove the top frame from the Runtime stack
     * @see interpreter.RunTimeStack#popFrame()
     */
    public void popRunStackFrame() {
        runStack.popFrame();
    }

    /**
     * Enables or Disables DUMP mode based on the passed value
     * @param doDump Boolean value dictating the state of the DUMP mode
     * @see interpreter.RunTimeStack#dump()
     */
    public void dumpRunStack(Boolean doDump) {
        dump = doDump;
    }

    /**
     * Get the size/length of the Runtime stack
     * @return Size of the Runtime stack
     * @see interpreter.RunTimeStack#size()
     */
    public int runStackSize() {
        return runStack.size();
    }


    //----{ returnAddrs methods }-----------------------------------------------

    /**
     * Removes the last return address added.
     * @return Top item in the returnAddrs stack
     */
    public int popReturnAddrs() {
        return returnAddrs.pop();
    }

    /**
     * Adds a return address to keep track of
     * @param address the return address that is to be tracked
     * @return The address just added
     */
    public int pushReturnAddrs(int address) {
        return returnAddrs.push(address);
    }
    
}
