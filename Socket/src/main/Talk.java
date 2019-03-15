package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Talk extends JFrame {

	private JPanel contentPane;
	private static JTextField textField;
	private static JTextArea textArea;
	private int id;
	private static String username;
	private Socket socket;
	private static Socket socket_server;
	private static ServerSocket serverSocket;
	private BufferedReader reader ;
	private static PrintWriter writer;
	private static BufferedReader reader_server ;
	private static PrintWriter writer_server;
	private static Scanner scanner = new Scanner(System.in);
	private static Scanner scanner_server = new Scanner(System.in);
	private static boolean send = false;
	private static boolean breakdown=false;
	/**
	 * @wbp.nonvisual location=384,234
	 */

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Talk frame = new Talk(2,"bb",serverSocket);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public Talk(int id,String username,ServerSocket serverSocket) {
		setTitle("聊天");
		this.id=id;
		this.username=username;
		this.serverSocket=serverSocket;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 503, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		JLabel lblNewLabel = new JLabel("聊 天 内 容");
		lblNewLabel.setBounds(177, 14, 102, 20);
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 17));
		
		textField = new JTextField();
		textField.setBounds(37, 321, 264, 40);
		textField.setColumns(10);
		
		//boolean send = false;
		JButton btnNewButton = new JButton("发  送");
		btnNewButton.setBounds(309, 326, 102, 29);
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 17));
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				send=true;
			}
		});
		
		
		JButton btnNewButton_2 = new JButton("断开连接");
		btnNewButton_2.setBounds(131, 397, 111, 27);
		btnNewButton_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				breakdown=true;
			}
		});
		
		
		
		
		contentPane.setLayout(null);
		//contentPane.add(textArea);
		contentPane.add(textField);
		contentPane.add(btnNewButton);
		contentPane.add(btnNewButton_2);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 48, 383, 240);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		//System.out.println("1112");			
		try {
			//System.out.println("1113");
			//int port = getPort(id);
			
			runsocket();
			//System.out.println("1114");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					startTheSocket1();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}
	
	
	
/////////////////////////////////////////////	
	
	public void runsocket() throws IOException{
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//System.out.println("1111");
				try {
					startTheSocket();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
	  }

	////////////////////////////////////////客户端
	  //开始连接服务端
	  public void startTheSocket() throws IOException{
	      toConnectTheServer();//创建Socket并初始化
	      openTheThreadToReceiveInfoFromServer();//开启一个新的线程来接受服务端发来的信息
	      String yourMessage=" ";
	      //一个循环，用于读取键盘的输入
	      while(true) {
	    	  yourMessage =textField.getText();
	    	  //String yourMessage = scanner.nextLine();
	        //不为空则发送信息
	        if(yourMessage!=null&&send) {
	          textArea.append("\n"+"你说:"+yourMessage);
	          writer.println(yourMessage);
	          writer.flush();//记得flush清空缓冲区
	          send = false;
	        //判断是否退出
		        if (yourMessage.equals("exit")||breakdown) {
		          disConnect();
		          //System.exit(0);
		        }
	        }
	        /*//判断是否退出
	        if (yourMessage.equals("exit")) {
	          disConnect();
	          System.exit(0);
	        }*/
	      }
	  }
	  
	  

	  //创建一个Socket来连接本机的18080端口的服务端并初始化reader和writer
	  public void toConnectTheServer() throws IOException {
		 //System.out.println("id="+id+"\n");
		int port = getPort(username);
		System.out.println("port="+port+"\n");
	    socket=new Socket("127.0.0.1",port);
	    textArea.append("等待链接！");
	    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
	  }
	  
	 
	  public void openTheThreadToReceiveInfoFromServer() throws IOException {
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
	  public void printMessage() throws IOException {
		  boolean flag = false;
	    while (true) {
	      String messageFromServer = null;
	      try {
	        messageFromServer = reader.readLine();//读取信息
	        if(messageFromServer.equals("conn")) {
	        	textArea.append("\n"+"连接成功");
	        	flag=true;
	        }
	       /* if(flag) {
	        	textArea.append("\n"+username+":"+messageFromServer);
	        }*/
	        //System.out.println("He say:" + messageFromServer);//打印信息
	      } catch (IOException e) {
	        e.printStackTrace();
	      } catch (NullPointerException e) {
	 
	      }
	      if(messageFromServer.equals("exit")){
	        disConnect();//关闭连接
	        //System.exit(0);
	      }
	    }
	  }
	 
	  //关闭各种
	  public void disConnect() throws IOException {
	    socket.close();
	    reader.close();
	    writer.close();
	  }
	
///////////////////////////////////服务器端
	  
	  public static void startTheSocket1() throws IOException {
		   //serverSocket=new ServerSocket(18000); //打开18080端口等待连接
		   acceptTheSocket();//接受连接并且初始化Socket，reader，writer
		   openTheNewThreadToGetMessageFromClient();
		  /* while(true) {   
		      getYourMessage();//获取键盘数据并作为输入
		   }*/
		   while(true) {
			   if(breakdown) {
				   disConnect_server();
				   breakdown=false;
				   break;
			   }
		   }
		   
		 }
		 
		 
		 
		 
		 //接受连接并且初始化Socket，reader，writer
		 private static void acceptTheSocket() throws IOException {
		   socket_server=serverSocket.accept();//阻塞方法，用于接受Socket连接
		   System.out.println("Some one connect me ，it is "+socket_server.getLocalSocketAddress());//打印地址
		   System.out.println("It's port is "+socket_server.getPort());//打印端口号
		   reader_server = new BufferedReader(new InputStreamReader(socket_server.getInputStream()));//获得输入流
		   writer_server = new PrintWriter(new OutputStreamWriter(socket_server.getOutputStream()));//获得输出流
		   String conn = "conn";
		   writer_server.println(conn);//发送信息
		   writer_server.flush();//flush方法用于清空缓冲区，注意的是如果不flush你的信息可能发不出去一直存留在缓冲区
		 }
		 //扫描键盘并输入数据
		 private static void getYourMessage() throws IOException {
		   
		   //String yourMessage = scanner_server.nextLine();
			 String yourMessage =textField.getText();
		   checkYourMessage(yourMessage);//检查发送给客户端的信息
		   if(send) {
		   writer.println(yourMessage);//发送信息
		   writer.flush();//flush方法用于清空缓冲区，注意的是如果不flush你的信息可能发不出去一直存留在缓冲区
		   }
		 }
		 //用于检查是否退出
		 private static void checkYourMessage(String yourMessage) throws IOException {
		   //关闭各种
		   if (yourMessage.equals("exit")) {
		     socket_server.close();
		     writer_server.close();
		     reader_server.close();
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
		       messageFromClient = reader_server.readLine();
		     } catch (IOException e) {
		       e.printStackTrace();
		     }
		     printTheMessage(messageFromClient);//打印客户端发来的信息
		   }
		 }
		 //打印客户端发来的信息
		 private static void printTheMessage(String messageFromClient) {
		   if(messageFromClient!=null)
		     //System.out.println("He say:"+messageFromClient);
			   //textArea.append("\n"+"他说"+":"+messageFromClient);
			   textArea.append("\n"+username+":"+messageFromClient);
		 }
		 
		 private static void disConnect_server() {
			 /*try {
				socket_server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		     writer_server.close();
		     try {
				reader_server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 }  
	  
///////////////////////////////////	  
	
	  public int getPort(String name) {
			String driver1 = "com.mysql.jdbc.Driver";		
			String url = "jdbc:mysql://localhost:3306/socket";
			String username = "root";
			String password = "cc111";	
			Connection conn = null;
			Statement stmt = null;
			
			try{
			      //STEP 2: Register JDBC driver
			      Class.forName(driver1);
			      conn = DriverManager.getConnection(url, username, password);
			      System.out.println("Connected database successfully...");
			      stmt = conn.createStatement();
			      String sql = "select * from users where username = "+"\'"+name+"\'";
					// 执行 SQL语句
					// ResultSet 接收查询返回的结果集的一个接口 result 定义的一个接收返回结果的变量名
			      ResultSet result=null;
				  result = stmt.executeQuery(sql);
					// 将查询返回的结果集进行遍历输出
				  while (result.next()) {
						// t_id 必须跟数据库的 字段名一致 getInt() 这个 是由对应字段的数据类型决定
						return Integer.valueOf(result.getString("port"));
					}
				  
			   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }finally{
			      //finally block used to close resources
			      try{
			         if(stmt!=null)
			            conn.close();
			      }catch(SQLException se){
			      }// do nothing
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }//end finally try
			   }//end try
			
			return -1;
			
			
			//return 1;
		}
}