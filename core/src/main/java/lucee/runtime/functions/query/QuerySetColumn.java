/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
/**
 * Implements the CFML Function queryaddcolumn
 */
package lucee.runtime.functions.query;

import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.ext.function.BIF;
import lucee.runtime.op.Caster;
import lucee.runtime.type.Array;
import lucee.runtime.type.ArrayImpl;
import lucee.runtime.type.Collection;
import lucee.runtime.type.KeyImpl;
import lucee.runtime.type.Query;
import lucee.runtime.type.QueryColumn;

public final class QuerySetColumn extends BIF {

	private static final long serialVersionUID = -268309857190767441L;

	public static String call(PageContext pc, Query query, String columnName, String newColumnName) throws PageException {
		columnName = columnName.trim();
		newColumnName = newColumnName.trim();
		Collection.Key src = KeyImpl.init(columnName);
		Collection.Key trg = KeyImpl.init(newColumnName);

		Query qp = Caster.toQuery(query, null);
		if (qp != null) qp.rename(src, trg);
		else {
			QueryColumn qc = query.removeColumn(src);
			Array content = new ArrayImpl();
			int len = qc.size();
			for (int i = 1; i <= len; i++) {
				content.setE(i, qc.get(i, null));
			}
			query.addColumn(trg, content, qc.getType());
		}
		return null;
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		return call(pc, Caster.toQuery(args[0]), Caster.toString(args[1]), Caster.toString(args[2]));
	}
}