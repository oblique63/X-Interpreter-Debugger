package interpreter;

import java.util.HashMap;


/**
 * Stores the mapped values of ByteCode names to their respective classes
 * @author Enrique Gavidia
 */
public class CodeTable {
    private static HashMap<String, String> codeMap = new HashMap<String, String>();

    // List of the codes for whom class names are to be dynamically generated
    // by the 'init' method
    private static String[] codes = new String[] {
        "Args","Bop","Call","Dump","Falsebranch","Goto","Halt","Label",
        "Lit","Load","Pop","Read","Return","Store","Write",
        "Formal","Function","Line",
    };

    /**
     * Initiates the Hash table that pairs the ByteCodes to their respective
     * classes
     */
    public static void init() {
        // Dynamically generates each ByteCode class name based on the code names
        // stored in the 'codes' array
        String codeClass;
        for (String code : codes) {
            codeClass = code+"Code";
            code = code.toUpperCase();
            codeMap.put(code,codeClass);
        }
    }

    /**
     * Gets the class corresponding to the given ByteCode name
     * @param code ByteCode name
     * @return The respective ByteCode class
     */
    public static String get(String code) {
        return codeMap.get(code);
    }
}
