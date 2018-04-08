package browser.vm.views;

import java.awt.event.WindowAdapter;
import java.util.List;

import core.*;

public class EntityListView extends ListView<Entity> {

	public EntityListView(WindowAdapter vcl, List<Searchable<Entity>> list, String header) {
		super(vcl, list, header);
	}

	@Override
	public void redrawList(List<Searchable<Entity>> listContent) {
		tableModel.setValueAt("inhalt", 0, 0);
		//XXX
	}

}
