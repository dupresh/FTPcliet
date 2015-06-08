import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPtask {
 
 /**
  * ������� ��� ��������� ������ ������ � ����� � ����� Folder
  * @param ftpAdress, ����� ��� �������
  * @param user, ����� � ���
  * @param password, ������ � ���
  * @param Folder, ����� ��� ����� ������������� �����
  * @return String[] result, ������ ������/�����
  */
 
    public static String[] GetList(String ftpAdress, String user, String password,String Folder)
    {
        String[] result;
        result = null;
 
        FTPClient client = new FTPClient();
 
        try {
             client.connect(ftpAdress);
             client.login(user, password);
             client.changeWorkingDirectory(Folder);
             result = client.listNames();
             client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

/**
 * Upload a file to a FTP server
  * @param ftpAdress, ����� ��� �������
  * @param user, ����� � ���
  * @param password, ������ � ���
 * @param PathOnFtp, ���� � ����� �� ��� - �������� /upload/touch.dat
 * @param FilenameOnLocalMachine, ���� � ����� �� ��������� ������ - �������� C:/somefile.txt
 */
 
    public static void UploadFileOnFtp(String ftpAdress, String user, String password,String PathOnFtp, String FilenameOnLocalMachine)
    {
        FTPClient client = new FTPClient();
 
        FileInputStream fis = null;
 
        try {
        client.connect(ftpAdress);
        client.login(user, password);
 
        // Create an InputStream of the file to be uploaded

        String filename = FilenameOnLocalMachine;
        fis = new FileInputStream(filename);
 
 
        // Store file to server
        
        client.storeFile(PathOnFtp, fis);
        client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 /**
  * Change directory
  * @param ftpAdress, ����� ��� �������
   * @param user, ����� � ���
   * @param password, ������ � ���
   * @param ChangeFolder, �����, � ������� ����� ������� , �������� /upload
  */
    public static void FTPChangeDir(String ftpAdress, String user, String password, String ChangeFolder){
     
        FTPClient client = new FTPClient();
 
        try {
 
            client.connect(ftpAdress);
            client.login(user, password);
 
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Connect failed");
                return;
            }
 
            boolean success = client.login(user, password);
            showServerReply(client);
 
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            }
 
            // Changes working directory
            success = client.changeWorkingDirectory(ChangeFolder);
            showServerReply(client);
 
            if (success) {
                System.out.println("Successfully changed working directory.");
            } else {
                System.out.println("Failed to change working directory. See server's reply.");
            }
 
            // logs out
            client.logout();
            client.disconnect();
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

 
    private static void showServerReply(FTPClient �lient) {
        String[] replies = �lient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    
 /**
  * Download File from FTP
   * @param ftpAdress, ����� ��� �������
   * @param user, ����� � ���
   * @param password, ������ � ���
  * @param FullPathToPutFile - ������ ���� � ����� �� ��������� ������, ���� ����� ��� ���������
  * @param FilenameOnFTP - ��� ����� �� ��� (������� ���� ���� � ����� /upload/)
  */
    
 public static void DownloadFileFromFTP(String ftpAdress, String user, String password,String FullPathToPutFile, String FilenameOnFTP)
{
       
    FTPClient client = new FTPClient();
    FileOutputStream fos = null;

    try {
    client.connect(ftpAdress);
    client.login(user, password);


//       The remote filename to be downloaded.

    String filename = FullPathToPutFile;
    fos = new FileOutputStream(filename);


//       Download file from FTP server

    client.retrieveFile("/upload/"+FilenameOnFTP, fos);
    //
  
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if (fos != null) {
                fos.close();
            }
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
 


 public static void main(String[] args) {
  // �������������
  
     FTPChangeDir(args[0],args[1],args[2],"/upload");
     DownloadFileFromFTP(args[0],args[1],args[2],"C:/touch.dat","touch.dat");
     
     UploadFileOnFtp(args[0],args[1],args[2],"/upload/touch.dat","C:/1.xml");
     String[] result = GetList(args[0],args[1],args[2],"upload");
        for (String result1 : result) {
            System.out.println("Name = " + result1);
        }
      
    }
}