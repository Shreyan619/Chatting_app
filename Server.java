package Chatting_app;

import javax.swing.*;
import javax.swing.border.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class Server implements ActionListener {

    JTextField text;
    JPanel head;
    static Box vertical = new Box(BoxLayout.PAGE_AXIS);
    JPanel chatPanel;
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Server() {

        f.setLayout(null);

        head = new JPanel();
        head.setBackground(new Color(0x00a3cc));
        head.setBounds(0, 0, 450, 50);
        head.setLayout(null);
        ;
        f.add(head);

        // Back icon
        ImageIcon image = new ImageIcon(ClassLoader.getSystemResource("Chatting_app/icons/3.png"));
        Image img = image.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        // img cannot be imported to jlabel directly after scaling
        // it needs to be converted to a new obj
        ImageIcon i3 = new ImageIcon(img);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 10, 25, 25);
        head.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // setVisible(false);
                System.exit(0);
            }
        });

        // profile pic
        ImageIcon userImage = new ImageIcon(ClassLoader.getSystemResource("Chatting_app/icons/1.png"));
        Image Uimg = userImage.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i4 = new ImageIcon(Uimg);
        JLabel profile = new JLabel(i4);
        profile.setBounds(40, 10, 30, 30);
        head.add(profile);

        // video call icon
        ImageIcon videoImage = new ImageIcon(ClassLoader.getSystemResource("Chatting_app/icons/video.png"));
        Image Vimg = videoImage.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i5 = new ImageIcon(Vimg);
        JLabel Video = new JLabel(i5);
        Video.setBounds(250, 10, 25, 25);
        head.add(Video);

        // call icon
        ImageIcon PhoneImage = new ImageIcon(ClassLoader.getSystemResource("Chatting_app/icons/phone.png"));
        Image Pimg = PhoneImage.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(Pimg);
        JLabel Phone = new JLabel(i6);
        Phone.setBounds(300, 10, 25, 25);
        head.add(Phone);

        // three dots container pane
        ImageIcon threeDotsImage = new ImageIcon(ClassLoader.getSystemResource("Chatting_app/icons/3icon.png"));
        Image threeimg = threeDotsImage.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i7 = new ImageIcon(threeimg);
        JLabel threeDots = new JLabel(i7);
        threeDots.setBounds(350, 10, 10, 25);
        head.add(threeDots);

        // userName
        JLabel name = new JLabel("Henry");
        name.setBounds(85, 10, 200, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Roboto Slab", Font.BOLD, 15));
        head.add(name);

        // active now
        JLabel status = new JLabel("Active Now");
        status.setBounds(85, 25, 200, 20);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Roboto Slab", Font.BOLD, 9));
        head.add(status);

        // chatting ui
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBounds(5, 55, 370, 595);
        f.add(chatPanel);

        // send text area
        text = new JTextField();
        text.setBounds(5, 655, 325, 40);
        text.setFont(new Font("San Serif", Font.PLAIN, 17));
        f.add(text);

        // send button
        ImageIcon sendIcon = new ImageIcon(ClassLoader.getSystemResource("Chatting_app/icons/send.png"));
        JButton sendBtn = new JButton(sendIcon);
        sendBtn.setBounds(335, 655, 40, 40);
        sendBtn.addActionListener(this);
        sendBtn.setBackground(new Color(0x4cb314));
        f.add(sendBtn);

        f.setSize(380, 700);
        f.setUndecorated(true);
        f.setLocation(200, 50);
        f.setVisible(true);
        // setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void actionPerformed(ActionEvent e) {
        try {
            String message = text.getText();
            // JLabel output = new JLabel(message);

            JPanel p = formatLabel(message);
            // p.add(output);

            chatPanel.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            chatPanel.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(message);
            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();

        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    public static JPanel formatLabel(String message) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel(message);
        output.setFont(new Font("Tahoma", Font.PLAIN, 15));
        output.setBackground(new Color(0x0066ff));
        output.setForeground(Color.WHITE);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(10, 10, 10, 13));

        panel.add(output);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(date.format(calendar.getTime()));
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new Server();
        // ServerSocket skt = null;
        try {
            // create server
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();

                // DataInputStream recieves messages
                DataInputStream din = new DataInputStream(s.getInputStream());

                // DataOutputStream sends messages
                dout = new DataOutputStream(s.getOutputStream());

                while (true) {
                    String msg = din.readUTF(); // reads mssg from client
                    JPanel panel = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
