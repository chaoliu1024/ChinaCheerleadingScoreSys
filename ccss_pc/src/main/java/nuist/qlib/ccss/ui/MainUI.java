/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.ServerSocket;

import nuist.qlib.ccss.net.BroadcastIP;
import nuist.qlib.ccss.net.MainServerInputThread;
import nuist.qlib.ccss.net.ReceIP;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 总界面。调用打分界面、出场顺序界面、导出成绩界面
 * 
 * @author Chao Liu
 * @since ccss 1.0
 */
public class MainUI {

	protected Shell shell;

	private TabFolder tabFolder;

	private TabItem tabItem1;
	private TabItem tabItem2;
	private TabItem tabItem3;
	private TabItem tabItem4;
	private TabItem tabItem5;

	private Composite pan1;
	private Composite pan2;
	private Composite pan3;
	private Composite pan4;
	private Composite pan5;
	private Composite composite;
	public static ConfigPanel configPanel;

	private static Logger logger = LoggerFactory.getLogger(MainUI.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MainUI window = new MainUI();
			window.open();
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		center(shell);
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
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(1123, 710);
		shell.setText("啦啦操竞赛评分系统");
		shell.setImage(new Image(display, MainUI.class
				.getResourceAsStream("/img/logo.png")));
		// shell.setImage(new Image(display, "img/logo.png"));
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION
						| SWT.YES | SWT.NO);
				messagebox.setText("提示");
				messagebox.setMessage("您确定关闭系统吗?");
				int message = messagebox.open();
				if (message == SWT.YES) {
					e.doit = true;
					System.exit(0);
				} else {
					e.doit = false;
				}
			}
		});
		this.defaultInit();
	}

	/**
	 * 窗口居中
	 */
	private void center(Shell shell) {
		Monitor monitor = shell.getMonitor();
		Rectangle bounds = monitor.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	/**
	 * 初始化界面
	 */
	private void defaultInit() {

		try {
			// 打开时清空address.txt
			File f = new File("Address.txt");
			FileWriter fw = new FileWriter(f);
			fw.write("");
			fw.close();
			// 启动组播IP的线程
			MulticastSocket sendSocket;
			sendSocket = new MulticastSocket(9998);
			new Thread(new BroadcastIP(sendSocket, shell)).start();
			// 启动接收IP的线程
			MulticastSocket receSocket = new MulticastSocket(9999);
			new Thread(new ReceIP(receSocket)).start();
			// 启动接收打分信息的线程
			ServerSocket serverSocket = new ServerSocket(6666);
			MainServerInputThread mainServerInputThread = new MainServerInputThread(
					serverSocket);
			mainServerInputThread.start();
		} catch (IOException e1) {
			logger.error(e1.toString());
			e1.printStackTrace();
		}
		shell.setLayout(new FormLayout());

		composite = new Composite(shell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.left = new FormAttachment(0);
		fd_composite.bottom = new FormAttachment(0, 682);
		fd_composite.top = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		OnlinePanel.getInstance(shell, composite, SWT.NONE);

		tabFolder = new TabFolder(shell, SWT.BORDER);
		fd_composite.right = new FormAttachment(tabFolder, -6);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.bottom = new FormAttachment(composite, 0, SWT.BOTTOM);
		fd_tabFolder.right = new FormAttachment(0, 1256);
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.left = new FormAttachment(0, 154);
		tabFolder.setLayoutData(fd_tabFolder);
		tabFolder.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));

		pan1 = new Composite(tabFolder, SWT.None); // 面板1
		pan2 = new Composite(tabFolder, SWT.None); // 面板2
		pan3 = new Composite(tabFolder, SWT.None); // 面板3
		pan4 = new Composite(tabFolder, SWT.None); // 面板4
		pan5 = new Composite(tabFolder, SWT.None); // 面板5

		tabItem1 = new TabItem(tabFolder, SWT.NULL);
		tabItem1.setText("比赛成绩");
		tabItem1.setControl(pan1);
		MatchPanel.getInstance(shell, pan1, SWT.NONE);
		pan1.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("出场顺序制作");
		tabItem2.setControl(pan2);
		TeamSortPanel.getInstance(shell, pan2, SWT.NONE);
		pan2.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabItem3 = new TabItem(tabFolder, SWT.NONE);
		tabItem3.setText("导出成绩");
		tabItem3.setControl(pan3);
		ScoreToWebPanel.getInstance(shell, pan3, SWT.NONE);
		pan3.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabItem4 = new TabItem(tabFolder, SWT.NONE);
		tabItem4.setText("配置表");
		tabItem4.setControl(pan4);
		configPanel = ConfigPanel.getInstance(shell, pan4, SWT.NONE);
		pan4.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabItem5 = new TabItem(tabFolder, SWT.NONE);
		tabItem5.setText("数据处理");
		tabItem5.setControl(pan5);
		HandlerDataPanel.getInstance(shell, pan5, SWT.NONE);
		pan5.setLayout(new FillLayout(SWT.HORIZONTAL));
	}
}
