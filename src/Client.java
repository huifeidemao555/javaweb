import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8080)) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("已经连接到服务端！");
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            writer.write(scanner.nextLine() + '\n');
            writer.flush();
            System.out.println("收到服务端响应：");
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(reader.readLine());
        }catch (IOException e) {
            System.out.println("连接服务端失败！");
        }

    }
}