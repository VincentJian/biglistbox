package web.ui.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zkmax.zul.MatrixModel;
import org.zkoss.zul.AbstractListModel;

import data.dao.BigTableDAO;
import data.pojo.BigTable;

@SuppressWarnings("rawtypes")
public class MyMatrixModel<Head extends List, Row extends List, Cell, Header> extends AbstractListModel<Row> implements MatrixModel<Row, Head, Cell, Header> {

	private static final long serialVersionUID = 3693737900474094171L;

	private int _colSize;
	private int _rowSize;
	private int _beginOffset;
	private int _cacheSize = 200;
	private Map<String, MyKeyList<String>> _rowCache;
	private List<String> _headerData;
	
	public MyMatrixModel(int colSize, int rowSize) {
		_colSize = colSize;
		_rowSize = rowSize;
		_headerData = new MyKeyList<String>(colSize, 0, new Fun() {
			public Object apply(int index) {
				return "Column " + (index + 1);
			}});
	}
	
	public void setRowCache(int start, int end) {
		BigTableDAO dao = new BigTableDAO();
		List<BigTable> list = dao.findBetweenIds(start, end);
		_rowCache = new HashMap<String, MyKeyList<String>>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			final BigTable bigTable = (BigTable) iterator.next();
			MyKeyList<String> cells = new MyKeyList<String>(_colSize, start, new Fun() {
				public Object apply(int index) throws Exception {
					String name = "_column" + (index + 1);
					Field field = bigTable.getClass().getDeclaredField(name);
					field.setAccessible(true);
					return (String) field.get(bigTable);
				}
			});
			_rowCache.put(String.valueOf(start), cells);
			start++;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Row getElementAt(int index) {
		final String key = String.valueOf(index);
		if(_rowCache == null || index < _beginOffset || index >= _beginOffset + _cacheSize) {
			_beginOffset = index;
			setRowCache(index, index + _cacheSize);
		}
		return (Row) _rowCache.get(key);
	}

	public int getSize() {
		return _rowSize;
	}

	public int getColumnSize() {
		return _colSize;
	}

	public int getHeadSize() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	public Head getHeadAt(int rowIndex) {
		return (Head) _headerData;
	}

	@SuppressWarnings("unchecked")
	public Cell getCellAt(Row rowData, int columnIndex) {
		return (Cell) rowData.get(columnIndex);
	}

	@SuppressWarnings("unchecked")
	public Header getHeaderAt(Head headData, int columnIndex) {
		return (Header) headData.get(columnIndex);
	}
}
