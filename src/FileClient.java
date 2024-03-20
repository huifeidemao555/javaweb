import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        try {
            //*********1.连接socket*********
            socket.connect(new InetSocketAddress("localhost", 8080));
            Scanner scanner = new Scanner(System.in);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("已经成功连接到服务器...");
            System.out.println("********************************************");
            while(true) {
                //*********2.输入需要传输的文件名*********
                System.out.print("输入文件名: ");
                String filename = "";
                filename = scanner.nextLine();
                //*********3.将文件名发给server*********
                writer.write(filename + '\n');
                writer.flush();
                if(filename.equals("exit")) {
                    System.out.println("主动结束文件传输，已结束程序！");
                    break;
                }
                String str = reader.readLine();
                if(str.equals("Invalid filename, please input again!")) {
                    System.out.println(filename + "是无效的文件名，请重新输入! ");
                    continue;
                }
                //*********4.接收网络流并写入文件*********
                byte[] bytes = new byte[1024];
                int len;
                String path = "fileclient\\" + filename;
                File file = new File(path);
                if(!file.exists()) {
                    boolean created = file.createNewFile();
                    if(created) {
                        System.out.println("文件成功创建");
                    } else{
                        System.out.println("文件创建失败");
                    }
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while((len = socket.getInputStream().read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                    if(len < bytes.length) {
                        break;
                    }
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                System.out.println("文件传输完成!");
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
