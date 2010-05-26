package interpreter.debugger;

import interpreter.ByteCodeLoader;
import interpreter.CodeTable;
import java.io.IOException;

/**
 * Adjusts the normal ByteCodeLoader to recognize specialized Debug-Bytecodes
 * @author Enrique Gavidia
 */
public class DebugByteCodeLoader extends ByteCodeLoader {
    public DebugByteCodeLoader(String programPath) throws IOException {
        super(programPath);
    }

    /**
     * Decides whether a specialized Debug ByteCode is needed, and returns the
     * appropriate ByteCode class name
     * @param code Code string read from code file
     * @return ByteCode class name
     */
    @Override
    protected String getCodeClass(String code) {
        if (code.matches("FORMAL|FUNCTION|LINE|LIT|POP|RETURN"))
            return "debuggerByteCodes." + CodeTable.get(code);
        else
            return CodeTable.get(code);
    }
}
