import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    public static void main(String[] args) throws IOException {
        try(ServerSocket server = new ServerSocket(8080)) {
            //*********1.连接socket*********
            System.out.println("等待客户端连接...");
            Socket socket = server.accept();
            System.out.println("客户端已经连接,ip地址为: " + socket.getInetAddress().getHostAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String filename;
            System.out.println("********************************************");
            while(true) {
                //*********2.读取文件名*********
                filename = reader.readLine();
                System.out.println("客户端请求文件: " + filename);
                //*********3.根据收到文件名进行相应操作*********
                if(filename.equals("exit")) {
                    System.out.println("文件传输结束，退出程序！");
                    break;
                } else if(!findFile(filename)) {
                    System.out.println(filename + "不存在！");
                    writer.write("Invalid filename, please input again!" + '\n');
                    writer.flush();
                } else {
                    writer.write("Valid filaname" + '\n');
                    writer.flush();
                    //*********4.读取文件并发送网络字节流*********
                    String path = "fileserver\\" + filename;
                    File file = new File(path);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len;
                    while((len = fileInputStream.read(bytes)) != -1) {
                        socket.getOutputStream().write(bytes, 0, len);
                    }
                    socket.getOutputStream().flush();
                    fileInputStream.close();
                    System.out.println(filename + "文件传输完成");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean findFile(String filename) {
        String path = "fileserver\\" + filename;
        File file = new File(path);
        return file.exists();
    }
}
