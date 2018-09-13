package com.hyatte;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ReadRecord {
	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("*****************");
		System.out.println("请选择");
		xuanze();

	}

	private static void xuanze() throws FileNotFoundException, IOException {
		System.out.println("***1.�鿴��Ҽ�¼***");
		System.out.println("***2.�鿴���Լ�¼***");
		System.out.println("***3.ɾ����Ҽ�¼***");
		System.out.println("***4.ɾ�����Լ�¼***");
		System.out.print("�����룺");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		switch (choice) {
		case 1:
			readplayer();
			
			break;
		case 2:
			readcomputer();
		
			break;
		case 3:
			deleteplayer();

			break;
		case 4:
			deletecomputer();
	
			break;

		default:
			System.out.println("�������");
			xuanze();
			break;
		}
	}

	private static void readplayer() throws FileNotFoundException, IOException {
		/**
		 * һ��һ�ж�ȡ ����BWһ��һ��д��
		 */
		FileReader fr = new FileReader("record of player.txt");
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			System.out.println(s);
		}
		br.close();
		fr.close();
	}

	private static void readcomputer() throws IOException {
		/**
		 * һ��һ�ж�ȡ ����BWһ��һ��д��
		 */
		FileReader fr = new FileReader("record of computer.txt");
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		br.close();
		fr.close();
	}

	private static void deleteplayer() throws IOException {
		File f = new File("record of player.txt");
		f.delete();
		f.createNewFile();
	}

	private static void deletecomputer() throws IOException {
		File f = new File("record of computer.txt");
		f.delete();
		f.createNewFile();
	}
}
