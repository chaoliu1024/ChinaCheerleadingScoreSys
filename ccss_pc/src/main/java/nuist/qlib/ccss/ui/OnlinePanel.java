/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import nuist.qlib.ccss.dao.OnlineDao;
import nuist.qlib.ccss.net.BroadcastIP;
import nuist.qlib.ccss.net.MainClientOutputThread;
import nuist.qlib.ccss.net.ReceIP;
import nuist.qlib.ccss.util.file.AddressManager;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 显示裁判是否在线
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class OnlinePanel extends Composite {

	private static OnlinePanel panel;
	private ExpandBar bar;
	private ExpandItem judgeExpandItem1;
	private ExpandItem judgeExpandItem2;
	private ExpandItem chiefJudgeExpandItem;
	private Composite composite_1;
	private Composite composite_2;
	private Composite composite_3;
	private Label judge01Label1;
	private Label judge02Label1;
	private Label judge03Label1;
	private Label judge04Label1;
	private Label judge05Label1;
	private Label judge06Label1;
	private Label judge07Label1;
	private Label judge08Label1;
	private Label judge09Label1;
	private Label judge10Label1;
	private Label judge01Label2;
	private Label judge02Label2;
	private Label judge03Label2;
	private Label judge04Label2;
	private Label judge05Label2;
	private Label judge06Label2;
	private Label judge07Label2;
	private Label judge08Label2;
	private Label judge09Label2;
	private Label judge10Label2;
	private Label chiefJudge01Label;
	private Label chiefJudge02Label;
	private Label chiefJudge03Label;
	private Boolean isStop = true;
	private String[] numChars = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10" };
	private OnlineDao onlineDao;
	private AddressManager adressManager = new AddressManager();// 地址管理

	public OnlinePanel(final Shell shell, final Composite parent,
			final int style) {
		super(parent, style);
		this.setLayout(new FillLayout());
		bar = new ExpandBar(this, SWT.V_SCROLL);
		bar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		// First item
		judgeExpandItem1 = new ExpandItem(bar, SWT.NONE, 0);
		judgeExpandItem1.setExpanded(true);
		// Second item
		judgeExpandItem2 = new ExpandItem(bar, SWT.NONE, 1);
		judgeExpandItem2.setExpanded(true);
		// Third item
		chiefJudgeExpandItem = new ExpandItem(bar, SWT.NONE, 2);
		chiefJudgeExpandItem.setExpanded(true);

		bar.setSpacing(8);

		new Thread() {// 检测时间以便确定上线还是下线
			String judgeName = null;
			String chiefJudgeName = null;
			int judgeNum1 = 0;
			int judgeNum2 = 0;
			int chiefJudgeNum = 0;
			boolean isLink = BroadcastIP.getLink();// 历史网络连接状态
			boolean curLink = BroadcastIP.getLink();// 当前网络连接状态
			MouseListener mouseListener = new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					Label label = (Label) e.getSource();
					AdjustDialog mb = new AdjustDialog(shell, label.getText());
					mb.open();
				}

				@Override
				public void mouseDown(MouseEvent e) {
				}

				@Override
				public void mouseUp(MouseEvent e) {
				}
			};

			public void run() {
				while (isStop) {
					curLink = BroadcastIP.getLink();
					if (isLink != curLink) { // 网络连接状态发生变化
						if (!isLink) { // 变为联网状态,线程暂停10秒，等到各裁判发送IP
							try {
								Thread.sleep(10 * 1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						isLink = curLink;
					}
					onlineDao = new OnlineDao();
					long currentTime = System.currentTimeMillis();
					long[] timer = ReceIP.getTimer();
					judgeNum1 = 0;
					judgeNum2 = 0;
					chiefJudgeNum = 0;
					int curOnlineJudgeNum1 = 0;
					int curOnlineJudgeNum2 = 0;
					int curOnlineChiefJudgeNum = 0;
					int curOfflineJudgeNum1 = 0;
					int curOfflineJudgeNum2 = 0;
					int curOfflineChiefJudgeNum = 0;
					int lineState[] = new int[timer.length];

					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							if (composite_1 != null) {
								composite_1.dispose();
							}
							composite_1 = new Composite(bar, SWT.NONE);
							composite_1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							GridLayout layout = new GridLayout();
							layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
							layout.verticalSpacing = 10;
							composite_1.setLayout(layout);

							judge01Label1 = new Label(composite_1, SWT.NONE);
							judge01Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge02Label1 = new Label(composite_1, SWT.NONE);
							judge02Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge03Label1 = new Label(composite_1, SWT.NONE);
							judge03Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge04Label1 = new Label(composite_1, SWT.NONE);
							judge04Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge05Label1 = new Label(composite_1, SWT.NONE);
							judge05Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge06Label1 = new Label(composite_1, SWT.NONE);
							judge06Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge07Label1 = new Label(composite_1, SWT.NONE);
							judge07Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge08Label1 = new Label(composite_1, SWT.NONE);
							judge08Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge09Label1 = new Label(composite_1, SWT.NONE);
							judge09Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge10Label1 = new Label(composite_1, SWT.NONE);
							judge10Label1.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));

							if (composite_2 != null) {
								composite_2.dispose();
							}
							composite_2 = new Composite(bar, SWT.NONE);
							composite_2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							GridLayout layout2 = new GridLayout();
							layout2.marginLeft = layout2.marginTop = layout2.marginRight = layout2.marginBottom = 10;
							layout2.verticalSpacing = 10;
							composite_2.setLayout(layout2);

							judge01Label2 = new Label(composite_2, SWT.NONE);
							judge01Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge02Label2 = new Label(composite_2, SWT.NONE);
							judge02Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge03Label2 = new Label(composite_2, SWT.NONE);
							judge03Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge04Label2 = new Label(composite_2, SWT.NONE);
							judge04Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge05Label2 = new Label(composite_2, SWT.NONE);
							judge05Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge06Label2 = new Label(composite_2, SWT.NONE);
							judge06Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge07Label2 = new Label(composite_2, SWT.NONE);
							judge07Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge08Label2 = new Label(composite_2, SWT.NONE);
							judge08Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge09Label2 = new Label(composite_2, SWT.NONE);
							judge09Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							judge10Label2 = new Label(composite_2, SWT.NONE);
							judge10Label2.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));

							if (composite_3 != null) {
								composite_3.dispose();
							}
							composite_3 = new Composite(bar, SWT.NONE);
							composite_3.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							GridLayout layout3 = new GridLayout();
							layout3.marginLeft = layout3.marginTop = layout3.marginRight = layout3.marginBottom = 10;
							layout3.verticalSpacing = 10;
							composite_3.setLayout(layout3);

							chiefJudge01Label = new Label(composite_3, SWT.NONE);
							chiefJudge01Label.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							chiefJudge02Label = new Label(composite_3, SWT.NONE);
							chiefJudge02Label.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
							chiefJudge03Label = new Label(composite_3, SWT.NONE);
							chiefJudge03Label.setBackground(SWTResourceManager
									.getColor(SWT.COLOR_WHITE));
						}
					});

					for (int i = 0; i < timer.length; i++) {
						if (currentTime - timer[i] <= 30 * 1000) {// 若相隔小于等于30s(IP发送时间的两倍)或不相等,则表示该角色在线
							if (i < 10) {
								judgeNum1++; // 在线裁判组一裁判统计
							} else if (i < 20) {
								judgeNum2++; // 在线裁判组二裁判统计
							} else {
								chiefJudgeNum++; // 在线裁判长统计
							}
							lineState[i] = 1;
						} else { // 若相隔大于10s(IP发送时间的两倍)或相等,则表示该角色不在线
							lineState[i] = 0;
						}
					}
					// 根据统计情况设置label
					for (int j = 0; j < lineState.length; j++) {
						if (lineState[j] == 0) {// 不在线
							if (j < 10) {
								curOfflineJudgeNum1++;
								switch (curOfflineJudgeNum1 + judgeNum1) {
								case 1:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge01Label1
															.setText(judgeName);
													judge01Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 2:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge02Label1
															.setText(judgeName);
													judge02Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 3:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge03Label1
															.setText(judgeName);
													judge03Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 4:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge04Label1
															.setText(judgeName);
													judge04Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 5:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge05Label1
															.setText(judgeName);
													judge05Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 6:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge06Label1
															.setText(judgeName);
													judge06Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 7:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge07Label1
															.setText(judgeName);
													judge07Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 8:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge08Label1
															.setText(judgeName);
													judge08Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 9:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge09Label1
															.setText(judgeName);
													judge09Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 10:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge10Label1
															.setText(judgeName);
													judge10Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								default:
									break;
								}
							} else if (j < 20) {
								curOfflineJudgeNum2++;
								switch (curOfflineJudgeNum2 + judgeNum2) {
								case 1:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge01Label2
															.setText(judgeName);
													judge01Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 2:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge02Label2
															.setText(judgeName);
													judge02Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 3:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge03Label2
															.setText(judgeName);
													judge03Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 4:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge04Label2
															.setText(judgeName);
													judge04Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 5:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge05Label2
															.setText(judgeName);
													judge05Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 6:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge06Label2
															.setText(judgeName);
													judge06Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 7:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge07Label2
															.setText(judgeName);
													judge07Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 8:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge08Label2
															.setText(judgeName);
													judge08Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 9:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge09Label2
															.setText(judgeName);
													judge09Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								case 10:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge10Label2
															.setText(judgeName);
													judge10Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(judgeName,
												false);
									}
									break;
								default:
									break;
								}
							} else if (j < 23) {
								curOfflineChiefJudgeNum++;
								switch (curOfflineChiefJudgeNum + chiefJudgeNum) {
								case 1:
									chiefJudgeName = "裁判长" + numChars[j - 20];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													chiefJudge01Label
															.setText(chiefJudgeName);
													chiefJudge01Label
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(
												chiefJudgeName, false);
									}
									break;
								case 2:
									chiefJudgeName = "裁判长" + numChars[j - 20];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													chiefJudge02Label
															.setText(chiefJudgeName);
													chiefJudge02Label
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(
												chiefJudgeName, false);
									}
									break;
								case 3:
									chiefJudgeName = "裁判长" + numChars[j - 20];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													chiefJudge03Label
															.setText(chiefJudgeName);
													chiefJudge03Label
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_GRAY));
												}
											});
									if (curLink) {
										onlineDao.updateLoginStatus(
												chiefJudgeName, false);
									}
									break;
								default:
									break;
								}

							}
						} else if (lineState[j] == 1) {// 在线
							if (j < 10) {
								curOnlineJudgeNum1++;
								switch (curOnlineJudgeNum1) {
								case 1:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge01Label1
															.setText(judgeName);
													judge01Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge01Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 2:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge02Label1
															.setText(judgeName);
													judge02Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge02Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 3:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge03Label1
															.setText(judgeName);
													judge03Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge03Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 4:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge04Label1
															.setText(judgeName);
													judge04Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge04Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 5:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge05Label1
															.setText(judgeName);
													judge05Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge05Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 6:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge06Label1
															.setText(judgeName);
													judge06Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge06Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 7:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge07Label1
															.setText(judgeName);
													judge07Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge07Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 8:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge08Label1
															.setText(judgeName);
													judge08Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge08Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 9:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge09Label1
															.setText(judgeName);
													judge09Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge09Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 10:
									judgeName = "打分裁判1-" + numChars[j];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge10Label1
															.setText(judgeName);
													judge10Label1
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge10Label1
															.addMouseListener(mouseListener);
												}
											});
									break;
								default:
									break;
								}
							} else if (j < 20) {
								curOnlineJudgeNum2++;
								switch (curOnlineJudgeNum2) {
								case 1:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge01Label2
															.setText(judgeName);
													judge01Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge01Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 2:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge02Label2
															.setText(judgeName);
													judge02Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge02Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 3:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge03Label2
															.setText(judgeName);
													judge03Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge03Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 4:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge04Label2
															.setText(judgeName);
													judge04Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge04Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 5:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge05Label2
															.setText(judgeName);
													judge05Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge05Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 6:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge06Label2
															.setText(judgeName);
													judge06Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge06Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 7:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge07Label2
															.setText(judgeName);
													judge07Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge07Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 8:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge08Label2
															.setText(judgeName);
													judge08Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge08Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 9:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge09Label2
															.setText(judgeName);
													judge09Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge09Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 10:
									judgeName = "打分裁判2-" + numChars[j - 10];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													judge10Label2
															.setText(judgeName);
													judge10Label2
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													judge10Label2
															.addMouseListener(mouseListener);
												}
											});
									break;
								default:
									break;
								}
							} else if (j < 23) {
								curOnlineChiefJudgeNum++;
								switch (curOnlineChiefJudgeNum) {
								case 1:
									chiefJudgeName = "裁判长" + numChars[j - 20];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													chiefJudge01Label
															.setText(chiefJudgeName);
													chiefJudge01Label
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													chiefJudge01Label
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 2:
									chiefJudgeName = "裁判长" + numChars[j - 20];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													chiefJudge02Label
															.setText(chiefJudgeName);
													chiefJudge02Label
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													chiefJudge02Label
															.addMouseListener(mouseListener);
												}
											});
									break;
								case 3:
									chiefJudgeName = "裁判长" + numChars[j - 20];
									Display.getDefault().syncExec(
											new Runnable() {
												public void run() {
													chiefJudge03Label
															.setText(chiefJudgeName);
													chiefJudge03Label
															.setForeground(SWTResourceManager
																	.getColor(SWT.COLOR_BLUE));
													chiefJudge03Label
															.addMouseListener(mouseListener);
												}
											});
									break;
								default:
									break;
								}
							}
						}
					}
					onlineDao.close();// 关闭数据库连接
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							judgeExpandItem1.setText("裁判组一 [" + judgeNum1
									+ "/10]");
							composite_1.redraw();
							judgeExpandItem1.setHeight(composite_1.computeSize(
									SWT.DEFAULT, SWT.DEFAULT).y);
							judgeExpandItem1.setControl(composite_1);
							judgeExpandItem1.setExpanded(judgeExpandItem1
									.getExpanded());
							judgeExpandItem2.setText("裁判组二 [" + judgeNum2
									+ "/10]");
							composite_2.redraw();
							judgeExpandItem2.setHeight(composite_1.computeSize(
									SWT.DEFAULT, SWT.DEFAULT).y);
							judgeExpandItem2.setControl(composite_2);
							judgeExpandItem2.setExpanded(judgeExpandItem2
									.getExpanded());
							chiefJudgeExpandItem.setText("裁判长  ["
									+ chiefJudgeNum + "/10]");
							chiefJudgeExpandItem.setHeight(composite_3
									.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
							chiefJudgeExpandItem.setControl(composite_3);
							chiefJudgeExpandItem
									.setExpanded(chiefJudgeExpandItem
											.getExpanded());
						}
					});

					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						isStop = false;
					}
				}
			}
		}.start();
	}

	/**
	 * 调整分数
	 */
	class AdjustDialog extends Dialog {
		private String role;
		private Shell shell;

		public AdjustDialog(Shell shell, String role) {
			super(shell);
			this.role = role;
			this.shell = shell;
		}

		protected Point getInitialSize() {
			return new Point(245, 145);
		}

		@Override
		protected Point getInitialLocation(Point initialSize) {

			Point location = new Point(760, 380);
			return location;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite container = new Composite(parent, SWT.NONE);
			Label label = new Label(container, SWT.NONE);
			label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
			label.setForeground(SWTResourceManager
					.getColor(SWT.COLOR_LIST_SELECTION));
			label.setBounds(70, 20, 100, 27);
			label.setText(role);
			Button upScore = new Button(container, SWT.ARROW | SWT.UP
					| SWT.BORDER);
			upScore.setBounds(10, 75, 52, 27);
			upScore.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
			upScore.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String commandReceiver[] = { toEnglishName(role) };
					MainClientOutputThread mainClientOutputThread = new MainClientOutputThread();
					int sum = mainClientOutputThread.sendCommand(
							commandReceiver, "up");
					if (sum == -1) {// 发送失败
						adressManager.clearIP();
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageBox box = new MessageBox(shell, SWT.OK);
								box.setText("提示");
								box.setMessage("发送失败，请重发！");
								int val = box.open();
								if (val == SWT.OK)
									return;
							}
						});
					} else if (sum == 0) {
						// 无ip
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageBox box = new MessageBox(shell, SWT.OK);
								box.setText("提示");
								box.setMessage("未获得接收地址，请稍等片刻！");
								int val = box.open();
								if (val == SWT.OK)
									return;
							}
						});
					}
				}
			});
			Button downScore = new Button(container, SWT.ARROW | SWT.DOWN
					| SWT.BORDER);
			downScore.setBounds(96, 75, 52, 27);
			downScore.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					toEnglishName(role);
					String commandReceiver[] = { toEnglishName(role) };
					MainClientOutputThread mainClientOutputThread = new MainClientOutputThread();
					int sum = mainClientOutputThread.sendCommand(
							commandReceiver, "down");
					if (sum == -1) {// 发送失败
						adressManager.clearIP();
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageBox box = new MessageBox(shell, SWT.OK);
								box.setText("提示");
								box.setMessage("发送失败，请重发！");
								int val = box.open();
								if (val == SWT.OK)
									return;
							}
						});
					} else if (sum == 0) {// 无ip
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageBox box = new MessageBox(shell, SWT.OK);
								box.setText("提示");
								box.setMessage("未获得接收地址，请稍等片刻！");
								int val = box.open();
								if (val == SWT.OK)
									return;
							}
						});
					}
				}
			});
			Button ok = new Button(container, SWT.BORDER);
			ok.setBounds(179, 75, 52, 27);
			ok.setText("确认");
			ok.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					toEnglishName(role);
					String commandReceiver[] = { toEnglishName(role) };
					MainClientOutputThread mainClientOutputThread = new MainClientOutputThread();
					int sum = mainClientOutputThread.sendCommand(
							commandReceiver, "ok");
					if (sum == -1) {// 发送失败
						adressManager.clearIP();
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageBox box = new MessageBox(shell, SWT.OK);
								box.setText("提示");
								box.setMessage("发送失败，请重发！");
								int val = box.open();
								if (val == SWT.OK)
									return;
							}
						});
					} else if (sum == 0) {// 无ip
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								MessageBox box = new MessageBox(shell, SWT.OK);
								box.setText("提示");
								box.setMessage("未获得接收地址，请稍等片刻！");
								int val = box.open();
								if (val == SWT.OK)
									return;
							}
						});
					}
					close();
				}
			});

			return container;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
		}

		public String toEnglishName(String roleName) {
			String chineseName[] = { "打分裁判1-01", "打分裁判1-02", "打分裁判1-03",
					"打分裁判1-04", "打分裁判1-05", "打分裁判1-06", "打分裁判1-07", "打分裁判1-08",
					"打分裁判1-09", "打分裁判1-10", "打分裁判2-01", "打分裁判2-02", "打分裁判2-03",
					"打分裁判2-04", "打分裁判2-05", "打分裁判2-06", "打分裁判2-07", "打分裁判2-08",
					"打分裁判2-09", "打分裁判2-10", "裁判长01", "裁判长02", "裁判长03" };
			String englishName[] = { "judge1-01", "judge1-02", "judge1-03",
					"judge1-04", "judge1-05", "judge1-06", "judge1-07",
					"judge1-08", "judge1-09", "judge1-10", "judge2-01",
					"judge2-02", "judge2-03", "judge2-04", "judge2-05",
					"judge2-06", "judge2-07", "judge2-08", "judge2-09",
					"judge2-10", "chiefJudge01", "chiefJudge02", "chiefJudge03" };
			for (int i = 0; i < chineseName.length; i++) {
				if (roleName.equals(chineseName[i])) {
					return englishName[i];
				}
			}
			return null;
		}
	}

	/**
	 * 获取唯一实例
	 * 
	 * @param shell
	 * @param parent
	 * @param style
	 * @return
	 */
	public static OnlinePanel getInstance(Shell shell, Composite parent,
			int style) {
		if (panel == null) {
			panel = new OnlinePanel(shell, parent, style);
		}
		return panel;
	}
}
