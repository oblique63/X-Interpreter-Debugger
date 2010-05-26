/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interpreter.debugger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Used to load the source code from a given file on to a List object
 * @author Enrique Gavidia
 */
public class SourceCodeLoader {
    private SourceCodeLoader() {}

    /**
     * Loads lines of source code from a file on to a List of SourceLineEntries
     * @param codeFile The file where the source code is stored
     * @return A list of SourceLineEntry objects containing the lines of the source code
     * @throws FileNotFoundException
     * @throws IOException
     * @see interpreter.debugger.SourceLineEntry
     */
    public static List<SourceLineEntry> load(String codeFile) throws FileNotFoundException, IOException {

        BufferedReader programFile = new BufferedReader(new FileReader(codeFile));
        List<SourceLineEntry> sourceCode = new Vector<SourceLineEntry>();
        SourceLineEntry line;
        while (programFile.ready())
            sourceCode.add( new SourceLineEntry(programFile.readLine(), false) );

        return sourceCode;
    }

}
