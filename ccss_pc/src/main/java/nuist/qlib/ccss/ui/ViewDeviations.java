/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.text.DecimalFormat;
import java.util.HashMap;

import nuist.qlib.ccss.manager.score.QueryScore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * 查看误差界面
 * 
 * @author Wang Fang
 * @since ccss 1.0
 */
public class ViewDeviations {
	protected Shell shell;
	private Table table;
	protected static String matchName;
	protected static int matchType; // 赛事模式
	private QueryScore score;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(764, 117);
		shell.setImage(new Image(shell.getDisplay(), ViewDeviations.class
				.getResourceAsStream("/img/logo.png")));
		shell.setText(matchName + "---" + "裁判打分误差");
		Rectangle displayBounds = shell.getDisplay().getPrimaryMonitor()
				.getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);

		score = new QueryScore();

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 731, 62);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(71);
		tblclmnNewColumn.setText("裁判1");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(71);
		tblclmnNewColumn_1.setText("裁判2");

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(71);
		tblclmnNewColumn_2.setText("裁判3");

		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(71);
		tblclmnNewColumn_3.setText("裁判4");

		TableColumn tblclmnNewColumn_4 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_4.setWidth(71);
		tblclmnNewColumn_4.setText("裁判5");

		TableColumn tblclmnNewColumn_5 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_5.setWidth(71);
		tblclmnNewColumn_5.setText("裁判6");

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(71);
		tableColumn.setText("裁判7");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(71);
		tableColumn_1.setText("裁判8");

		TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(71);
		tableColumn_2.setText("裁判9");

		TableColumn tableColumn_3 = new TableColumn(table, SWT.NONE);
		tableColumn_3.setWidth(71);
		tableColumn_3.setText("裁判10");

		TableItem item;
		if (score.isCollected()) {
			HashMap<String, Object> one = score.getDeviations(matchName,
					matchType);
			DecimalFormat format = new DecimalFormat("#0.0000");
			if (one != null) {
				item = new TableItem(table, SWT.NONE);
				item.setText(new String[] {
						format.format(one.get("error1")),
						format.format(one.get("error2")),
						format.format(one.get("error3")),
						format.format(one.get("error4")),
						format.format(one.get("error5")),
						format.format(one.get("error6")),
						format.format(one.get("error7")),
						format.format(one.get("error8")),
						format.format(one.get("error9")),
						one.get("error10") == null ? "" : format.format(one
								.get("error10")) });
			}
			if (score.isCollected()) {
				score.close();
			}
		} else {
			MessageBox box = new MessageBox(shell);
			box.setText("警告");
			box.setMessage("数据库连接失败!!");
			box.open();
		}
	}
}
