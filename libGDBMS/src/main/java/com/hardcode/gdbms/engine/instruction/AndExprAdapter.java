package com.hardcode.gdbms.engine.instruction;

import com.hardcode.gdbms.engine.values.Value;


/**
 * Adapta una expresi�n AND
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class AndExprAdapter extends AbstractExpression implements Expression {
	/**
	 * Evalua expresi�n invocando el m�todo adecuado en funci�n del tipo de
	 * expresion (suma, producto, ...) de los objetos Value de la expresion,
	 * de las subexpresiones y de los objetos Field
	 *
	 * @param row Fila en la que se eval�a la expresi�n, en este caso no es
	 * 		  necesario, pero las subexpresiones sobre las que se opera pueden
	 * 		  ser campos de una tabla, en cuyo caso si es necesario
	 *
	 * @return Objeto Value resultado de la operaci�n AND de la expresi�n
	 * 		   representada por el nodo sobre el cual �ste objeto es adaptador
	 *
	 * @throws EvaluationException Si se produce un error
	 */
	public Value evaluate(long row) throws EvaluationException {
		Value ret = null;

		Adapter[] expr = getChilds();

		if (expr.length > 0) {
			ret = ((Expression) expr[0]).evaluateExpression(row);

			for (int i = 1; i < expr.length; i++) {
				try {
                    ret = ret.and(((Expression) expr[i]).evaluateExpression(row));
                } catch (IncompatibleTypesException e) {
                    throw new EvaluationException(getClass().getName(),e);
                }
			}
		}

		return ret;
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Expression#getFieldName()
	 */
	public String getFieldName() {
		return null;
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Expression#isLiteral()
	 */
	public boolean isLiteral() {
		return Utilities.checkExpressions(getChilds());
	}

	/**
	 * @see com.hardcode.gdbms.engine.instruction.Expression#simplify()
	 */
	public void simplify() {
		Adapter[] childs = getChilds();

		if (childs.length == 1) {
			getParent().replaceChild(this, childs[0]);
		}
	}
}
