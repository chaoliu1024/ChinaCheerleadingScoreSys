/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuist.qlib.ccss.manager.team.FinalTeamExport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 导出决赛数据导航
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class FinalMatchDialog extends Dialog {
	private static Text text;
	private Shell dialog;
	private List<HashMap<String, Object>> data;
	private FinalTeamExport teamExport;
	private MessageBox box;

	FinalMatchDialog(Shell parent, List<HashMap<String, Object>> data,
			MessageBox box) {
		super(parent);// 调用基类的构造方法
		this.data = data;
		this.box = box;
	}

	// 将对话框打开
	public void open() {
		teamExport = new FinalTeamExport();

		Display display = Display.getDefault();
		final Shell parent = this.getParent();
		dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setSize(250, 154);
		dialog.setText("决赛数据导航");
		dialog.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				if (teamExport.isCollected()) {
					teamExport.close();
				}
				dialog.dispose();
			}
		});
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = dialog.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		dialog.setLocation(x, y);

		Label label = new Label(dialog, SWT.NONE);
		label.setBounds(10, 39, 61, 17);
		label.setText("导出人数：");

		text = new Text(dialog, SWT.BORDER);
		text.setBounds(81, 36, 133, 23);

		Button button = new Button(dialog, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textString = text.getText();
				if (textString == null || textString.trim().equals("")) {
					box.setMessage("信息不完整，请检查!");
					box.open();
				} else {
					int textInt = Integer.valueOf(textString);
					if (textInt > data.size()) {
						box.setMessage("选择的人数超过该项目已有人数!");
						box.open();
					} else if (textInt <= 0) {
						box.setMessage("选择的人数必须大于0!");
						box.open();
					} else {
						// 完成导出功能
						dialog.close();
						if (teamExport.isCollected()) {
							List<HashMap<String, Object>> realData = new ArrayList<HashMap<String, Object>>();
							for (int i = textInt - 1; i >= 0; i--) {
								realData.add(data.get(i));
							}
							int result = teamExport.setFinalTeam(realData);
							if (result == -1) {
								box.setMessage("决赛队伍导入失败!");
								box.open();
							} else {
								box.setMessage("决赛队伍导入成功!");
								box.open();
							}
							teamExport.close();
						} else {
							box.setMessage("数据库连接失败!");
							box.open();
						}
					}
				}
			}
		});
		button.setBounds(154, 76, 80, 27);
		button.setText("导出");
		dialog.open();
		while (!dialog.isDisposed()) {
			if (display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
