package interpreter.debugger;

import java.util.HashMap;
import java.util.Set;

/**
 * Object that stores each entry in the DebugSymbolTable
 */
class Binder {
  private Object value;
  private String prevtop;   // prior symbol in same scope
  private Binder tail;      // prior binder for same symbol
                            // restore this when closing scope
  Binder(Object v, String p, Binder t) {
	value=v; prevtop=p; tail=t;
  }

  Object getValue() { return value; }
  String getPrevtop() { return prevtop; }
  Binder getTail() { return tail; }
}

/**
 * Symbol table that stores a variable IDs, and their offset in the runtime stack
 */
public class DebugSymbolTable {
  private HashMap<String,Binder> symbols;
  private String top;

  public DebugSymbolTable(){}


 /**
  * Gets the object associated with the specified symbol in the Table.
  */
  public int get(String key) {
	int e = (Integer) symbols.get(key).getValue();
	return e;
  }

 /**
  * Puts the specified value into the Table, bound to the specified Symbol.<br>
  * Maintain the list of symbols in the current scope (top);<br>
  * Add to list of symbols in prior scope with the same string identifier
  */
  public void put(String key, int value) {
      symbols.put(key, new Binder(value, top, symbols.get(key)));
      top = key;
  }


  public void beginScope() {
      symbols = new HashMap<String,Binder>();
      top=null;
  }

  public void popValues(int numOfPops) {
	for (int i = 0; i < numOfPops; i++) {
            Binder e = symbols.get(top);
            if (e.getTail()!=null)
               symbols.put(top,e.getTail());
	   else
               symbols.remove(top);
	   top = e.getPrevtop();
        }
  }

  /**
   * @return a set of the Table's symbols.
   */
  public Set<String> keys() {return symbols.keySet();}
}
