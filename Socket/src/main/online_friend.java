package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class online_friend extends JFrame {

	private JPanel contentPane;
	private int id1=0;
	private String username1;
	private JComboBox comboBox;
	private Socket socket;
	private ServerSocket serverSocket;
	private BufferedReader reader ;
	private PrintWriter writer;
	private Scanner scanner = new Scanner(System.in);

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public online_friend(final int id,final String username,final ServerSocket serverSocket) {
		id1=id;
		username1=username;
		this.serverSocket=serverSocket;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JList list = new JList();
		
		JLabel lblNewLabel = new JLabel("在 线 好 友");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 19));
		
		comboBox = new JComboBox();
		//ResultSet rs = Selectlogin();
		Selectlogin();
		
		
		JButton btnNewButton = new JButton("聊天");
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String name = (String)comboBox.getSelectedItem();
						Talk talkframe = new Talk(id1,name,serverSocket);
						talkframe.setVisible(true);
					}
					
					
				}).start();
				
				/*String name = (String)comboBox.getSelectedItem();
				Talk talkframe = new Talk(id1,name,serverSocket);
				talkframe.setVisible(true);*/
				
				
			}
		});

		JLabel lblNewLabel_1 = new JLabel("选择好友:");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 17));
		
		JButton btnNewButton_1 = new JButton("退出登录");
		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				exit(id);
				
			}
		});
		
		JButton btnNewButton_2 = new JButton("刷新");
		btnNewButton_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				comboBox.removeAllItems();
				Selectlogin();
			}
		});
		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(list, GroupLayout.PREFERRED_SIZE, 422, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addGap(24))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(comboBox, Alignment.LEADING, 0, 117, Short.MAX_VALUE))
							.addGap(57)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton_2, 0, 0, Short.MAX_VALUE)))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(list)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1))
					.addGap(33)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(btnNewButton_2))
					.addGap(65)
					.addComponent(btnNewButton)
					.addContainerGap(49, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	

	public boolean locklogin(int id) {			
	        String driver1 = "com.mysql.jdbc.Driver";		
			String url = "jdbc:mysql://localhost:3306/socket";
			String username = "root";
			String password = "cc111";	
			Connection conn = null;
			Statement stmt2 = null;
			
			try{
			      //STEP 2: Register JDBC driver
			      Class.forName(driver1);
			      conn = DriverManager.getConnection(url, username, password);
			      System.out.println("Connected database successfully...");
			      stmt2 = conn.createStatement();
			      String sql = "update users set is_online= 0 where id= "+"\'"+id+"\'";
			      int result= 0;
				  result = stmt2.executeUpdate(sql);
					// 将查询返回的结果集进行遍历输出
				  if(result==1)
					  return true;
				  
			   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }finally{
			      //finally block used to close resources
			      try{
			         if(stmt2!=null)
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
			
			return false;
		}
		
		
	public void Selectlogin() {
			
	        String driver1 = "com.mysql.jdbc.Driver";		
			String url = "jdbc:mysql://localhost:3306/socket";
			String username = "root";
			String password = "cc111";	
			Connection conn = null;
			Statement stmt1 = null;
			ResultSet result=null;
			
			try{
			      //STEP 2: Register JDBC driver
			      Class.forName(driver1);
			      conn = DriverManager.getConnection(url, username, password);
			      System.out.println("Connected database successfully...");
			      stmt1 = conn.createStatement();
			      String sql = "select * from users where is_online = 1";
				  result = stmt1.executeQuery(sql);
				  try {
						while(result.next()) {
							comboBox.addItem(result.getString("username"));
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
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
			         if(stmt1!=null)
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
			
			//return result;
		}	
	
	
	public void exit(int id) {
		//public boolean updateUser(String username1) {
			
	        String driver1 = "com.mysql.jdbc.Driver";		
			String url = "jdbc:mysql://localhost:3306/socket";
			String username = "root";
			String password = "cc111";	
			Connection conn = null;
			Statement stmt1 = null;
			ResultSet result=null;
			
			try{
			      //STEP 2: Register JDBC driver
			      Class.forName(driver1);
			      conn = DriverManager.getConnection(url, username, password);
			      System.out.println("Connected database successfully...");
			      stmt1 = conn.createStatement();
			      String sql = "update users set is_online = 0 where id = "+id;
				  int mid = stmt1.executeUpdate(sql);
				  
			   }catch(SQLException se){
			      se.printStackTrace();
			   }catch(Exception e){
			      e.printStackTrace();
			   }finally{
			      try{
			         if(stmt1!=null)
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
		}
	
	
	
		
	
	public int getPort(int id) {
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
		      String sql = "select * from users where id = "+id;
				// 执行 SQL语句
				// ResultSet 接收查询返回的结果集的一个接口 result 定义的一个接收返回结果的变量名
		      ResultSet result=null;
			  result = stmt.executeQuery(sql);
				// 将查询返回的结果集进行遍历输出
			  while (result.next()) {
					// t_id 必须跟数据库的 字段名一致 getInt() 这个 是由对应字段的数据类型决定
					return Integer.valueOf(result.getString("id"));
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
