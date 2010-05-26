package interpreter;

import interpreter.bytecodes.ByteCode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Reads ByteCodes from a given file, and loads them on to a Program object
 * @see interpreter.Program
 * @author Enrique Gavidia
 */
public class ByteCodeLoader {
    private BufferedReader programFile;

    /**
     * Loads the file to be read
     * @param programPath Path of the input ByteCode file
     * @throws IOException
     */
    public ByteCodeLoader(String programPath) throws IOException {
        programFile = new BufferedReader(new FileReader(programPath));
    }
    
    /**
     * Reads the ByteCodes from the input file, and loads them on to a Program object
     * @return Program object with the ByteCodes from the file loaded onto it
     * @see interpreter.Program
     */
    public Program loadCodes() {
        Program program = new Program();
        try {
            while (programFile.ready()) {
                String line = programFile.readLine();

                // For each line read, separate the bytecode from its arguments,and store the 2 strings
                // in an array (where code[0] is the bytecode and code[1] is the string of its arguments)
                String[] code = line.split("\\s",2);
                
                String codeClass = "interpreter.bytecodes." + getCodeClass(code[0]);
                ByteCode bytecode = (ByteCode)(Class.forName(codeClass).newInstance());

                if (code.length > 1)
                    bytecode.init(code[1]);
                else
                    bytecode.init("");
                
                program.addCode(bytecode);
            }
        } catch (Exception e) {}

        program.resolveAddresses();
        return program;
    }

    /**
     * Returns the ByteCode's class name; designed to be overrided by
     * debugByteCodeLoader
     * @param code Code string read from code file
     * @return ByteCode class name
     */
    protected String getCodeClass(String code) {
        return CodeTable.get(code);
    }
}
