package caverspark.bio.example2;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @date 2020/8/29 15:58
 * @author chengjiaqing
 * @version : 0.1
 */ 
 
//经过多个telnet客户端测试，这种方式只能为一个客户端服务，与客户端断开连接后，才能服务下一个客户端
//因为TCP通信是点对点的，也就是1对1的方式
public class TraditionalSocketDemo {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7777);
        System.out.println("服务端启动");

        while(true){

            //获取socket套接字
            //accept()阻塞点
            //前一个客户端连接没有断开之前，不会连接下一个客户端
            Socket socket = serverSocket.accept();
            System.out.println("有新客户端连接上来了");

            //从客户端获取输入流
            InputStream is = socket.getInputStream();
            byte[] b = new byte[1024];

            while (true){
                //循环读取数据
                //read阻塞点
                int data = is.read(b);
                if(data != -1){
                    String info = new String(b,0,data,"GBK");
                    System.out.println(info);
                } else {
                    break;
                }
            }
        }
    }
}