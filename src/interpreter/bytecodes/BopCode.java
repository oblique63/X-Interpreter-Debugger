package interpreter.bytecodes;

import interpreter.VirtualMachine;

/**
 * Performs indicated binary operation on the top two values of the Runtime stack
 * @author Enrique Gavidia
 */
public class BopCode extends ByteCode {
    private String operator;
    private int result;
    public BopCode(){}

    @Override
    public void init(String args) {
        operator = args;
    }

    @Override
    public void execute(VirtualMachine vm) {
        int topInt = vm.popRunStack();
        int lowerInt = vm.popRunStack();
        
        result = computeResult(topInt,lowerInt,operator);

        vm.pushRunStack(result);
    }

    @Override
    public String getArgs() {
        return operator;
    }

    private int computeResult(int topInt, int lowerInt, String operation) {
        int res = 0;

        // Arithmetic operators
        if (operation.equals("+"))
            res = lowerInt + topInt;
        else if (operation.equals("-"))
            res = lowerInt - topInt;
        else if (operation.equals("*"))
            res = lowerInt * topInt;
        else if (operation.equals("/"))
            res = lowerInt / topInt;


        // Logical operators
        else if (operation.equals("=="))
            if (lowerInt == topInt)  res = 1;
            else  res = 0;

        else if (operation.equals("!="))
            if (lowerInt != topInt)  res = 1;
            else  res = 0;

        else if (operation.equals("<="))
            if (lowerInt <= topInt)  res = 1;
            else  res = 0;

        else if (operation.equals(">="))
            if (lowerInt >= topInt)  res = 1;
            else  res = 0;

        else if (operation.equals("<"))
            if (lowerInt < topInt)  res = 1;
            else  res = 0;

        else if (operation.equals(">"))
            if (lowerInt > topInt)  res = 1;
            else  res = 0;

        else if (operation.equals("&"))
            if ((lowerInt > 0) && (topInt > 0))  res = 1;
            else  res = 0;

        else if (operation.equals("|"))
            if ((lowerInt > 0) || (topInt > 0))  res = 1;
            else  res = 0;

        return res;
    }
}
