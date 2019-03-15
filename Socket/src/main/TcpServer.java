package main;

/*public class TcpServer {

}
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/**
* Created by Mezereon on 2017/3/1.
*/
public class TcpServer {
 private static Socket socket;
 private static ServerSocket serverSocket;
 private static BufferedReader reader;
 private static PrintWriter writer;
 private static Scanner scanner = new Scanner(System.in);
 //开启服务
 public static void run() throws IOException {
     startTheSocket();
  
 }
 
 public static void main(String args[]) {
	 try {
		run();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
 
 public static void startTheSocket() throws IOException {
   serverSocket=new ServerSocket(18000); //打开18080端口等待连接
   acceptTheSocket();//接受连接并且初始化Socket，reader，writer
   openTheNewThreadToGetMessageFromClient();
   while(true) {   
      getYourMessage();//获取键盘数据并作为输入
   }
 }
 
 
 
 
 //接受连接并且初始化Socket，reader，writer
 private static void acceptTheSocket() throws IOException {
   socket=serverSocket.accept();//阻塞方法，用于接受Socket连接
   System.out.println("Some one connect me ，it is "+socket.getLocalSocketAddress());//打印地址
   System.out.println("It's port is "+socket.getPort());//打印端口号
   reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//获得输入流
   writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));//获得输出流
   String conn = "conn";
   writer.println(conn);//发送信息
   writer.flush();//flush方法用于清空缓冲区，注意的是如果不flush你的信息可能发不出去一直存留在缓冲区
 }
 //扫描键盘并输入数据
 private static void getYourMessage() throws IOException {
   
   String yourMessage = scanner.nextLine();
   checkYourMessage(yourMessage);//检查发送给客户端的信息
   writer.println(yourMessage);//发送信息
   writer.flush();//flush方法用于清空缓冲区，注意的是如果不flush你的信息可能发不出去一直存留在缓冲区
 }
 //用于检查是否退出
 private static void checkYourMessage(String yourMessage) throws IOException {
   //关闭各种
   if (yourMessage.equals("exit")) {
     socket.close();
     writer.close();
     reader.close();
     System.exit(0);
   }
 }
 //开启一个新的线程来监听客户端发来的信息
 private static void openTheNewThreadToGetMessageFromClient() throws IOException {
   //开启一个新的线程
   new Thread(new Runnable() {
     @Override
     public void run() {
       getTheMessageFromClient();
     }
   }).start();
 
 }
 //不断循环来获取客户端发来的信息
 private static void getTheMessageFromClient() {
   while(true) {
     String messageFromClient = null;
     try {
       messageFromClient = reader.readLine();
     } catch (IOException e) {
       e.printStackTrace();
     }
     printTheMessage(messageFromClient);//打印客户端发来的信息
   }
 }
 //打印客户端发来的信息
 private static void printTheMessage(String messageFromClient) {
   if(messageFromClient!=null)
     System.out.println("He say:"+messageFromClient);
 } 
}
