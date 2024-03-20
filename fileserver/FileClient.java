import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("localhost", 8080));
            Scanner scanner = new Scanner(System.in);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("已经成功连接到服务器...");
            while(true) {
                System.out.print("input filename: ");
                String str = null;
                str = scanner.nextLine();
                writer.write(str);
                if(str == "exit") {
                    break;
                }
                str = reader.readLine();
                if(str.equals("Invalid filename, please input again!")) {
                    continue;
                }
                byte[] bytes = new byte[1024];
                int len;
                String path = "fileclient\\" + str;
                File file = new File(path);
                if(!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while((len = socket.getInputStream().read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
                System.out.println("finished file transmission!");
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
