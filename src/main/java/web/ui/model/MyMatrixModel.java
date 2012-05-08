package web.ui.model;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zkmax.zul.MatrixModel;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import data.dao.BigTableDAO;
import data.pojo.BigTable;

@SuppressWarnings("rawtypes")
public class MyMatrixModel<Row extends List, Head extends List, Cell, Header>
		extends AbstractListModel<Row> implements
		MatrixModel<Row, Head, Cell, Header>, Sortable<Cell> {

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
			}
		});
	}

	private void setRowCache(int start, int end) {
		BigTableDAO dao = new BigTableDAO();
		int startId = _sortDir ? start : (_rowSize - start),
			endId   = _sortDir ? end   : (_rowSize - end);

		List<BigTable> list = _sortDir ? dao.findBetweenIds(startId, endId,
				_sortDir) : dao.findBetweenIds(endId, startId, _sortDir);
		final List<String> fieldNames = new BigTable().getFieldsName();

		if (_rowCache == null)
			_rowCache = new HashMap<String, MyKeyList<String>>();
		_rowCache.clear();
		for (Iterator<BigTable> iterator = list.iterator(); iterator.hasNext();) {
			final BigTable bigTable = iterator.next();
			MyKeyList<String> cells = new MyKeyList<String>(_colSize, start,
					new Fun() {
						public Object apply(int index) throws Exception {
							Field field = bigTable.getClass().getDeclaredField(fieldNames.get(index));
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
		if (_rowCache == null || index < _beginOffset || index >= _beginOffset + _cacheSize) {
			_beginOffset = index - _cacheSize/2 < 0 ? 0 : index - _cacheSize/2;
			setRowCache(_beginOffset, _beginOffset + _cacheSize);
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

	private Comparator<Cell> _sorting;
	private boolean _sortDir = true;

	public void sort(Comparator<Cell> cmpr, boolean ascending) {
		System.out.println(_beginOffset);
		_sorting = cmpr;
		_sortDir = ascending;
		setRowCache(_beginOffset, _beginOffset + _cacheSize);
		fireEvent(ListDataEvent.STRUCTURE_CHANGED, -1, -1);
	}

	public String getSortDirection(Comparator<Cell> cmpr) {
		if (Objects.equals(_sorting, cmpr))
			return _sortDir ? "ascending" : "descending";
		return "natural";
	}
}
