import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("正在等待客户端连接...");
            Socket socket = server.accept();
            System.out.println("客户端已经连接，IP地址为: " + socket.getInetAddress().getHostAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("读取客户端数据...");
            String str = null;
            while((str = reader.readLine()) != null) {
                System.out.println(str);
            }
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write("已经收到"+'\n');
            writer.flush();
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
