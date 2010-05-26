package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Reads input from the user
 * @author Enrique Gavidia
 */
public class ReadCode extends ByteCode {
    public ReadCode(){}

    @Override
    public void init(String args){}
    
    @Override
    public void execute(VirtualMachine vm) {
        try {
            BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
            String num = in.readLine();
            vm.pushRunStack(Integer.parseInt(num));
            
        } catch( java.io.IOException ex ) {}
    }

    @Override
    public String getArgs() {
        return "";
    }
}
