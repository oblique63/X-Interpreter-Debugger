package interpreter;

import interpreter.debugger.SourceCodeLoader;
import interpreter.debugger.DebugByteCodeLoader;
import interpreter.debugger.DebugVM;
import interpreter.debugger.SourceLineEntry;
import interpreter.debugger.ui.DebuggerUI;
import java.io.*;
import java.util.List;

/**
 * <pre>
 * 
 *  
 *   
 *     Interpreter class runs the interpreter:
 *     1. Perform all initializations
 *     2. Load the bytecodes from file
 *     3. Run the virtual machine
 *     
 *   
 *  
 * </pre>
 */
public class Interpreter {
    private Boolean debugMode;
    private ByteCodeLoader bcl;
    private List<SourceLineEntry> sourceCode;

    public Interpreter(String codeFile, Boolean debug) {
        debugMode = debug;
        try {
            CodeTable.init();
            if (debugMode) {
                // assumes only program name is given
                String sourceFile = codeFile + ".x";
                codeFile += ".x.cod";
                bcl = new DebugByteCodeLoader(codeFile);
                sourceCode = SourceCodeLoader.load(sourceFile);
                System.out.println("****Debugging " + sourceFile + "****");
            } else {
                // assumes full path name is given
                bcl = new ByteCodeLoader(codeFile);
            }
	} catch (IOException e) {
            System.out.println("**** " + e);
	}
    }

    void run() {
        Program program = bcl.loadCodes();
        VirtualMachine vm;
        if (debugMode) {
            vm = new DebugVM(program, sourceCode);
            DebuggerUI.displayInterface((DebugVM) vm);
        } else {
            vm = new VirtualMachine(program);
            vm.executeProgram();
        }
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("***Incorrect usage, try: java interpreter.Interpreter <file>");
            System.exit(1);
        }

        Interpreter interpreter;
        if (args[0].equals("-d"))
            interpreter = new Interpreter(args[1], true);
        else
            interpreter = new Interpreter(args[0], false);

        interpreter.run();
    }
}