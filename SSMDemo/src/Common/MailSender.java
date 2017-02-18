package Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 邮件发送小工具
 * @author ctk
 *
 */
public class MailSender {
	public void mailSend(String rcptAddress,String content){
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader ins = null;
		try {
			socket = new Socket("smtp.163.com", 25);
			out = new PrintWriter(socket.getOutputStream());
			ins = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			String msg = "";
			System.out.println("收到:"+ins.readLine());
			
			//登录成功之后发送helo
			out.println("helo 163");
			out.flush();
			System.out.println("发送:helo 163");
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 250)
			{
				System.out.println("邮件服务器错误!");
				return;
			}
			//发送登录请求
			out.println("AUTH LOGIN");
			out.flush();
			System.out.println("发送:AUTH LOGIN");
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 334)
			{
				System.out.println("邮件服务器错误!");
				return;
			}
			
			//发送用户名
			out.println(MailSender.Base64Encoder(CommonInfo.emailAccount));
			out.flush();
			System.out.println("发送:"+CommonInfo.emailAccount);
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 334)
			{
				System.out.println("用户名错误!");
				return;
			}
			
			//发送密码
			out.println(MailSender.Base64Encoder(CommonInfo.emailPassword));
			out.flush();
			System.out.println("发送:"+CommonInfo.emailPassword);
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 235)
			{
				System.out.println("密码错误!");
				return;
			}
			
			//发件人
			out.println("mail from:<"+CommonInfo.emailAddress+">");
			out.flush();
			System.out.println("发送:"+"mail from:<"+CommonInfo.emailAddress+">");
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 250)
			{
				System.out.println("发件人必须为本账号!");
				return;
			}
			
			//收件人
			out.println("rcpt to:<"+rcptAddress+">");
			out.flush();
			System.out.println("发送:"+"rcpt to:<"+rcptAddress+">");
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 250)
			{
				System.out.println("收件人格式错误!");
				return;
			}
			
			//内容
			out.println("data");  
			out.flush();
			System.out.println("收到:"+ins.readLine());
			
			out.println("subject:激活码"); 
            out.println("from:"+CommonInfo.emailAddress); 
            out.println("to:"+rcptAddress);
            out.println("Content-Type:text/plain;charset=\"utf-8\"");
            out.println();
            out.println(content);//发送内容
            out.println(".");
            out.println("");
            out.flush();
			msg = ins.readLine();
			System.out.println("收到:"+msg);
			if(Integer.parseInt(msg.substring(0, 3)) != 250)
			{
				System.out.println("邮件格式不合法!");
				return;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
	            out.println("rset");
	            out.flush();
	            System.out.println("发送:rset");
	            System.out.println("收到:"+ins.readLine());  
	            out.println("quit");  
	            out.flush();
	            System.out.println("发送:quit");
	            System.out.println("收到:"+ins.readLine()); 
				socket.close();
				ins.close();
				out.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Base64加解密
	 * @param text
	 * @return
	 */
	public static String Base64Encoder(String text){
		byte[] b = null;
		try {
			b = text.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new BASE64Encoder().encode(b);
	}
	public static String Base64Decoder(String text){
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = null;
		String result = null;
		try {
			b = decoder.decodeBuffer(text);
			result = new String(b,"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
}
