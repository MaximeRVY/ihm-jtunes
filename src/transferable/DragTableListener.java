package transferable;

import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DragTableListener implements DragSourceListener, DragGestureListener{
	
	private JTable table;
	private DefaultTableModel modelTable;
	private DragSource dragSource = new DragSource();
	public DragTableListener(JTable table, DefaultTableModel modelTable) {
		this.table = table;
		this.modelTable = modelTable;
		DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(
				table, DnDConstants.ACTION_MOVE, this);
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dragGesture) {
		//(String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7)))
		StringSelection transferable = new StringSelection((String) (modelTable.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7)));
		dragSource.startDrag(dragGesture, DragSource.DefaultCopyDrop, transferable, this);
		
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		// TODO Auto-generated method stub
		
	}

}
