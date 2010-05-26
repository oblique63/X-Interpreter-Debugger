package interpreter;

import interpreter.bytecodes.ByteCode;
import java.util.List;
import java.util.Vector;


/**
 * Produces an object containing all the actual ByteCodes specified by the original
 * file input to the ByteCodeLoader object
 * @author Enrique Gavidia
 */
public class Program {
    private int codeNum;
    private List<ByteCode> codes;
    private List<Integer> labelIndexList;
    private List<Integer> codesToResolveIndexList;

    /**
     * Starts a new program
     */
    public Program() {
        codeNum = 0;
        codes = new Vector<ByteCode>();
        labelIndexList = new Vector<Integer>();
        codesToResolveIndexList = new Vector<Integer>();
    }

    /**
     * Adds a Bytecode object to the program
     * @param bytecode
     */
    public void addCode(ByteCode bytecode) {
        String codeName = bytecode.getName();

        // Keep track of where all the LABEL codes are located for future
        // reference when resolving addresses
        if (codeName.matches("LABEL"))
            labelIndexList.add(codeNum);
        else if (codeName.matches("FALSEBRANCH|GOTO|CALL|RETURN"))
            codesToResolveIndexList.add(codeNum);

        codes.add(bytecode);
        codeNum += 1;
    }

    /**
     * Gets the Bytecode object at the location given
     * @param codeNum index of the ByteCode requested
     * @return ByteCode at the given index
     */
    public ByteCode getCode(int codeNum) {
        return codes.get(codeNum);
    }

    /**
     * Used to find the specific addresses of LABELs that certain ByteCodes refer to
     */
    public void resolveAddresses() {
        // Iterate only through the codes that need to have their addresses resolved
        for (int codeIndex : codesToResolveIndexList) {
            ByteCode code = codes.get(codeIndex);
            String targetLabel = code.getArgs();

            // For each one, look up the corresponding LABEL Code in the program
            for (int labelIndex : labelIndexList) {
                String label = codes.get(labelIndex).getArgs();

                // Once the right LABEL Code is found, initiate the requested
                // bytecode again with the resolved address, and break out of
                // the loop to prevent unnecessary iteration
                if (label.matches(targetLabel)) {
                    String address = Integer.toString(labelIndex);
                    String arg = code.getArgs() + " " + address;
                    code.init(arg);
                    codes.set(codeIndex, code);
                    break;
                }
            }
        }
        
        
    }
}
