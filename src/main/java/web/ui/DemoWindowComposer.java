package web.ui;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Biglistbox;
import org.zkoss.zul.Window;

import web.ui.model.MyMatrixModel;
import web.ui.model.MyMatrixRenderer;

public class DemoWindowComposer extends SelectorComposer<Window> {

	private static final long serialVersionUID = 8151208890323726725L;
	@Wire("biglistbox")
	private Biglistbox biglist;
	
	@SuppressWarnings("rawtypes")
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		MyMatrixModel model = new MyMatrixModel(100, 22000);
		biglist.setModel(model);
		biglist.setColWidth("150px");
		biglist.setMatrixRenderer(new MyMatrixRenderer());
	}
}