/* CS 1501
   Alex Drizos
 */
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

public static final int PORT = 8765;

ObjectOutputStream objOut;
ObjectInputStream objIn;
BufferedReader myReader;
PrintWriter myWriter;
JTextArea outputArea;
JLabel prompt;
JTextField inputField;
String myName, serverName;
Socket connection;
SymCipher cipher;

public SecureChatClient ()
{
        try {

                myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
                serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
                InetAddress addr =
                        InetAddress.getByName(serverName);
                connection = new Socket(addr, PORT); // Connect to server with new
                                                     // Socket
                objOut = new ObjectOutputStream(connection.getOutputStream());
                objOut.flush(); //flush to prevent deadlocks

                objIn = new ObjectInputStream(connection.getInputStream());

                //receive server's public key E
                BigInteger publicKeyE = (BigInteger)objIn.readObject();
                //receive the server's public mod value N
                BigInteger modValN = (BigInteger)objIn.readObject();
                //receive the cipher type
                String cipherType = (String) objIn.readObject();
                //print cipher type to console for grader tracing
                System.out.println("Output For Grader: Cipher Type = " + cipherType);
                //create preferred cipher type object
                if (cipherType.equals("Sub")) {
                        cipher = new Substitute();
                }
                else if (cipherType.equals("Add")) {
                        cipher = new Add128();
                }

                //get key and convert to positive value using special constructor
                BigInteger key = new BigInteger(1, cipher.getKey());
                //print cipher key to console for grader tracing
                System.out.println("Output For Grader: Symetric Cipher Key = " + key);

                //RSA encrypt the key
                BigInteger encryptedKey = key.modPow(publicKeyE, modValN);
                //print E and N to the console for grader tracing
                System.out.println("Output For Grader: Key E = " + publicKeyE);
                System.out.println("Output For Grader: Key N = " + modValN);
                //send result to server
                objOut.writeObject(encryptedKey);

                //encrypt and send user name to server
                byte[] encryptedName = cipher.encode(myName);
                objOut.writeObject(encryptedName);




                this.setTitle(myName); // Set title to identify chatter

                Box b = Box.createHorizontalBox(); // Set up graphical environment for
                outputArea = new JTextArea(8, 30); // user
                outputArea.setEditable(false);
                b.add(new JScrollPane(outputArea));
                outputArea.setLineWrap(true);
                outputArea.setWrapStyleWord(true);

                outputArea.append("Welcome to the Chat Group, " + myName + "\n");

                inputField = new JTextField(""); // This is where user will type input
                inputField.addActionListener(this);

                prompt = new JLabel("Type your messages below:");
                Container c = getContentPane();

                c.add(b, BorderLayout.NORTH);
                c.add(prompt, BorderLayout.CENTER);
                c.add(inputField, BorderLayout.SOUTH);

                Thread outputThread = new Thread(this); // Thread is to receive strings
                outputThread.start();            // from Server

                addWindowListener(
                        new WindowAdapter()
                        {
                                public void windowClosing(WindowEvent e) {
                                        byte[] closing = cipher.encode("CLIENT CLOSING");
                                        try {
                                            objOut.writeObject(closing);
                                            System.exit(0);
                                        }
                                        catch (Exception h) {
                                            System.exit(-1);
                                        }
                                }
                        }
                        );

                setSize(500, 200);
                setVisible(true);

        }
        catch (Exception e)
        {
                System.out.println("Problem starting client!");
                e.printStackTrace();
        }
}

public void run()
{
        while (true)
        {
                try {
                        //read encrypted message
                        byte[] receivedMsg = (byte[]) objIn.readObject();
                        //print array of bytes to console for grader tracing
                        System.out.println("Output For Grader: Array of Bytes Received = " + receivedMsg);
                        //decode message
                        String decodedMsg = cipher.decode(receivedMsg);
                        //print decoded byte array for grader tracing
                        System.out.println("Output For Grader: Decoded Byte Array = " + decodedMsg.getBytes());
                        //print corresponding string for grader tracing
                        System.out.println("Output For Grader: Corresponding String = " + decodedMsg);

                        //post message to client's window
                        outputArea.append(decodedMsg+"\n");
                }
                catch (Exception e)
                {
                        System.out.println(e +  ", closing client!");
                        break;
                }
        }
        System.exit(0);
}

public void actionPerformed(ActionEvent e)
{
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        //print original string message to console for grader tracing
        System.out.println("Output For Grader: Original String Message = " + currMsg);
        //encode message
        byte[] encodedCurrMsg = cipher.encode(myName + ":" + currMsg);
        //print byte array corresponding to original string message for grader tracing
        System.out.println("Output For Grader: Byte Array of String Message = " + currMsg.getBytes());
        //print encrypted array of of bytes for grader tracing
        System.out.println("Output For Grader: Encrypted Array of Bytes = " + encodedCurrMsg);
        //send encoded message to server
        try {
            objOut.writeObject(encodedCurrMsg);
        }
        catch (Exception j) {
            System.out.println("Failed to write encoded message.  ¯\\_(ツ)_/¯");
        }

}

public static void main(String [] args)
{
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
}
}
