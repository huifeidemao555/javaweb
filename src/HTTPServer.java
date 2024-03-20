import javax.xml.xpath.XPath;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    public static void main(String[] args) throws IOException {
        /****************1.创建服务端套接字****************/
        ServerSocket server = new ServerSocket(8080);
        Socket socket = server.accept();
        System.out.println("*********************************************");
        System.out.println("客户端已经连接, IP为 " + socket.getInetAddress().getHostAddress());
        System.out.println("*********************************************");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        OutputStream outputStream = socket.getOutputStream();
        /****************2.从请求头中读取信息****************/
        String str = "";
        str = bufferedReader.readLine();
        if(str.contains("HTTP")) {
            System.out.println("建立新的HTTP连接");
        }
        String[] reqs = str.split(" ");
        String page = reqs[1].replace("/", "");
        System.out.println("用户请求的页面为" + page);

        String path = "static\\" + page;
        File file = new File(path);
        byte[] bytes = new byte[1024];
        int len = 0;
        FileInputStream fileInputStream = null;
        /****************3.根据请求页面做出响应****************/
        if(!file.exists()) {
            bufferedWriter.write("HTTP/1.1 404 Not Found\r\n");
            bufferedWriter.write("\r\n");
            fileInputStream = new FileInputStream(new File("static\\error.html"));

        } else {
            bufferedWriter.write("HTTP/1.1 200 OK\r\n");
            bufferedWriter.write("\r\n");
            fileInputStream = new FileInputStream(file);
        }
        bufferedWriter.flush();
        while((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        outputStream.flush();
        fileInputStream.close();
        server.close();
    }
}
