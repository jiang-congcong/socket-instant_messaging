package main;

/*public class TcpClient {

}
*/

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
 
/**
 * Created by Mezereon on 2017/2/27.
 */
public class TcpClient {
 
  private static Socket socket;
  private static BufferedReader reader ;
  private static PrintWriter writer;
  private static Scanner scanner = new Scanner(System.in);
  
  /*public static void main(String args[]) {
	  run();
  }*/
 
  //开启Socket来连接
  public static void run() throws IOException{
      startTheSocket();
  }
 
  //开始连接服务端
  private static void startTheSocket() throws IOException{
      toConnectTheServer();//创建Socket并初始化
      openTheThreadToReceiveInfoFromServer();//开启一个新的线程来接受服务端发来的信息
 
      //一个循环，用于读取键盘的输入
      while(true) {
        String yourMessage = scanner.nextLine();
        //不为空则发送信息
        if(yourMessage!=null) {
          writer.println(yourMessage);
          writer.flush();//记得flush清空缓冲区
        }
        //判断是否退出
        if (yourMessage.equals("exit")) {
          disConnect();
          System.exit(0);
        }
      }
  }
 
  //创建一个Socket来连接本机的18080端口的服务端并初始化reader和writer
  private static void toConnectTheServer() throws IOException {
    socket=new Socket("127.0.0.1",18080);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
  }
 
  private static void openTheThreadToReceiveInfoFromServer() throws IOException {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          printMessage();//打印从服务端收到的信息
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }
 
  //循环不断读取服务端的信息
  private static void printMessage() throws IOException {
    while (true) {
      String messageFromServer = null;
      try {
        messageFromServer = reader.readLine();//读取信息
        System.out.println("He say:" + messageFromServer);//打印信息
      } catch (IOException e) {
        e.printStackTrace();
      } catch (NullPointerException e) {
 
      }
      if(messageFromServer.equals("exit")){
        disConnect();//关闭连接
        System.exit(0);
      }
    }
  }
 
  //关闭各种
  private static void disConnect() throws IOException {
    socket.close();
    reader.close();
    writer.close();
  }
 
  public static void main(String args[]) {
	  try {
		run();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
}




