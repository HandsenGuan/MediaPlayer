package cn.guan.videoplayer.main;

import javax.swing.JFrame;  
import javax.swing.SwingUtilities;  

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;  

public class test {  

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;  

    public static void main(String[] args) {  
        SwingUtilities.invokeLater(new Runnable() {  
            @Override  
            public void run() {  
                new test();  
            }  
        });  
    }  

    private test() {  
        JFrame frame = new JFrame("vlcj Tutorial");  

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();  

        frame.setContentPane(mediaPlayerComponent);  

        frame.setLocation(100, 100);  
        frame.setSize(600, 400);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setVisible(true);  

        mediaPlayerComponent.getMediaPlayer().playMedia("d:\\a.mp4");// please change it to an existed media file  
    }  
} 
