/* Generated By:JJTree: Do not edit this line. ASTSQLSelectList.java */

package com.hardcode.gdbms.parser;

public class ASTSQLSelectList extends SimpleNode {
  public ASTSQLSelectList(int id) {
    super(id);
  }

  public ASTSQLSelectList(SQLEngine p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}