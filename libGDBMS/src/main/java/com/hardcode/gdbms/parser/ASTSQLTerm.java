/* Generated By:JJTree: Do not edit this line. ASTSQLTerm.java */

package com.hardcode.gdbms.parser;

public class ASTSQLTerm extends SimpleNode {
  public ASTSQLTerm(int id) {
    super(id);
  }

  public ASTSQLTerm(SQLEngine p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}