package caverspark.bio.example1;
/** 
 * @date 2020/5/20 23:47
 * @author chengjiaqing
 * @version : 0.1
 */

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        // 开启三个客户端，一个线程代表一个客户端
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        TestClient client = new TestClient("127.0.0.1", 8899);//指定服务端的地址和端口号
                        client.send(String.format("Hello,Server!I'm %d.这周末天气如何。", client.client.getLocalPort()));
                        client.receive();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ).start();
        }
    }

    /** * 测试客户端 */
    static class TestClient {

        public TestClient(String host, int port) throws Exception {
            // 与服务端建立连接
            this.client = new Socket(host, port);
            System.out.println("Cliect[port:" + client.getLocalPort() + "] 与服务端建立连接...");
        }

        private Socket client;
        private Writer writer;

        public void send(String msg) throws Exception {
            // 建立连接后就可以往服务端写数据了
            if(writer == null) {
                writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
            }
            writer.write(msg);
            writer.write("eof\n");
            writer.flush();
            // 写完后要记得flush
            System.out.println("Cliect[port:" + client.getLocalPort() + "] 消息发送成功");
        }

        public void receive() throws Exception {
            // 写完以后进行读操作
            Reader reader = new InputStreamReader(client.getInputStream(), "UTF-8");
            // 设置接收数据超时间为10秒
            client.setSoTimeout(10*1000);

            char[] chars = new char[64];
            int len;
            StringBuilder sb = new StringBuilder();
            //read(chars)方法也是阻塞的，没有读到任何数据会阻塞，所以要设置超时时间
            //读到eof结束
            while ((len = reader.read(chars)) != -1) {
                sb.append(new String(chars, 0, len));
            }
            System.out.println("Cliect[port:" + client.getLocalPort() + "] 消息收到了，内容:" + sb.toString());
            reader.close();
            // 关闭连接
            writer.close();
            client.close();
        }
    }
}
