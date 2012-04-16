package web.ui.model;

import java.util.Comparator;
import java.util.List;

import org.zkoss.zkmax.zul.MatrixComparatorProvider;

public class MyMatrixComparatorProvider<T> implements MatrixComparatorProvider<List<String>> {

	private static final long serialVersionUID = -1755361186270370642L;
	private int _x = -1;
	private boolean _acs;
	private MyComparator _cmpr;
	
	public MyMatrixComparatorProvider(boolean asc) {
		_acs = asc;
		_cmpr = new MyComparator(this);
	}
	
	public Comparator<List<String>> getColumnComparator(int columnIndex) {
		this._x = columnIndex;
		return _cmpr;
	}
	
	private class MyComparator implements Comparator<List<String>> {
		private MyMatrixComparatorProvider<T> _mmc;

		public MyComparator(MyMatrixComparatorProvider<T> mmc) {
			_mmc = mmc;
		}

		@Override
		public int compare(List<String> o1, List<String> o2) {
			return o1.get(_mmc._x).compareTo(o2.get(_mmc._x))
					* (_acs ? 1 : -1);
		}
	}
}
