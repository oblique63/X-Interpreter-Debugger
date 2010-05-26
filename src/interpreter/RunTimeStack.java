package interpreter;

import java.util.Stack;
import java.util.Vector;


/**
 * Runtime Stack data structure that contains the values produced by the ByteCodes
 * @author Enrique Gavidia
 */
public class RunTimeStack {
    private Vector<Integer> runStack;
    private Stack<Integer> framePointers;

    /**
     * Create a new RunTime stack
     */
    public RunTimeStack() {
        runStack = new Vector<Integer>();
        framePointers = new Stack<Integer>();
        framePointers.add(0);
    }

    /**
     * Prints out the contents of the stack, via formatted output of the
     * frames in the stack.
     */
    public void dump(){
        System.out.print("[");
        for (int i = 0; i < runStack.size(); i++) {
            if (i != 0 && framePointers.contains(i))
                System.out.print("] [");

            if (!framePointers.contains(i))
                System.out.print(",");
            
            System.out.print(runStack.elementAt(i));
        }        
        System.out.println("]");
    }

    /**
     * Gets the last element in the stack
     * @return Top element of the stack
     */
    public int peek() {
        return runStack.lastElement();
    }

    /**
     * Gets an element at a specified index in the RunTime stack
     * @param index Index of the requested element
     * @return The element at the given index
     */
    public int elementAt(int index) {
        return runStack.elementAt(index);
    }

    /**
     * Removes the last element in the stack
     * @return The item just removed
     */
    public int pop() {
        return runStack.remove(runStack.size()-1);
    }

    /**
     * Creates a new frame at the indicated index in the stack
     * @param offset Where to create the new frame
     */
    public void newFrameAt(int offset) {
        framePointers.add(offset);
    }

    /**
     * Removes the entire top frame from the stack
     */
    public void popFrame() {
        // Empty the contents of the frame, and add it's top element back to the 'runStack'
        int returnValue = runStack.lastElement();
        int frameIndex = framePointers.pop();
        int numOfRemovals = runStack.size() - frameIndex;
        for (int i = 0; i < numOfRemovals; i++)
            runStack.remove(frameIndex);

        runStack.add(returnValue);
    }

    /**
     * Gets the starting index of the top frame in the stack
     * @return Starting index of the top frame
     */
    public int peekFrame() {
        return framePointers.peek();
    }

    /**
     * Adds an int value to the RunTime stack
     * @param i Value to push
     * @return The value just pushed
     */
    public int push(int i) {
        runStack.add(i);
        return i;
    }

    /**
     * Adds an Integer value to the RunTime stack
     * @param i Value to push
     * @return The value just pushed
     */
    public Integer push(Integer i) {
        runStack.add(i);
        return i;
    }

    /**
     * Pops the top of the stack, and stores that value at the given offset
     * @param offset Where to store the 'popped' value
     * @return The value just stored
     */
    public int store(int offset) {
        // Overwrite the value at index 'offset' with the top element of the "stack", which is then removed.
        int frameOffset = framePointers.peek() + offset;
        runStack.set(frameOffset, runStack.lastElement());
        return runStack.remove(runStack.size()-1);
    }

    /**
     * Push the value at the given offset, to the top of the stack
     * @param offset The index of the value to be added to the top of the stack
     * @return The value just added
     */
    public int load(int offset) {
        int frameOffset = framePointers.peek() + offset;
        runStack.add(runStack.get(frameOffset));
        return runStack.lastElement();
    }

    /**
     * Gets the size of the RunTime stack
     * @return The size of the stack
     */
    public int size() {
        return runStack.size();
    }

    /**
     * Gets the number of frames contained in the Runtime Stack
     * @return number of frames
     */
    public int frames() {
        return framePointers.size();
    }
}
