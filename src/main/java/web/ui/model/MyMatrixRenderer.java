package web.ui.model;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkmax.zul.MatrixRenderer;

public class MyMatrixRenderer implements MatrixRenderer<List<String>> {

	public String renderCell(Component owner, List<String> data, int rowIndex, int colIndex) throws Exception {
		return data.get(colIndex);
	}

	public String renderHeader(Component owner, List<String> data, int rowIndex, int colIndex) throws Exception {
		return data.get(colIndex);
	}
}
