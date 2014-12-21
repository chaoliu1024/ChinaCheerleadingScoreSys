/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取、清空Adress.txt中的IP
 * 
 * @author Zhengfei Chen
 * @since ccss 1.0
 */
public class AddressManager {

	private FileWriter fw;
	private PrintWriter pw;
	private FileReader fr;
	private BufferedReader br;
	private String messageTemp;
	private List<String> list;

	/** 读取需要的IP */
	public List<String> getIP(String receiver[]) {
		try {
			fw = new FileWriter("Adress.txt", true);
			pw = new PrintWriter(fw);
			fr = new FileReader("Adress.txt");
			br = new BufferedReader(fr);
			list = new ArrayList<String>();

			while ((messageTemp = br.readLine()) != null) { // 遍历配置文件
				if (messageTemp.split("/")[0] == null
						|| messageTemp.split("/")[0].equals("")) {
					continue;
				} else {
					for (int i = 0; i < receiver.length; i++) {
						if (messageTemp.split("/")[0]
								.equalsIgnoreCase(receiver[i])) { // 根据角色名选择所需的IP
							String one = messageTemp.split("/")[1];
							list.add(one);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pw.close();
				fw.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/** 清空配置文件 (Adress里面IP冲突) */
	public void clearIP() {
		try {
			File f = new File("Adress.txt");
			fw = new FileWriter(f);
			fw.write("");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
