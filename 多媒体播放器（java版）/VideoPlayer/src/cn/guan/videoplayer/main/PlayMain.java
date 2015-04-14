package cn.guan.videoplayer.main;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import cn.guan.videoplayer.views.MainWindow;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class PlayMain {

	
	static MainWindow frame;
	
	public static void main(String[] args) {
		if (RuntimeUtil.isMac()) {
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"		//Macϵͳ��
					);
		}else if (RuntimeUtil.isWindows()) {
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "F:\\VLC_Player\\VideoLAN\\VLC"			//Windowϵͳ��F:\VLC_Player\VLC\sdk\lib\libvlc.lib
					);
		}else if (RuntimeUtil.isNix()) {
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib"			//Linuxϵͳ��
					);
		}
//		System.out.println(System.getProperty("java.library.path"));
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainWindow();
					frame.setVisible(true);
					
//					frame.getMediaPlayer().playMedia("D:\\1\\Breakup.mkv");
					
					String options[] = {"--subsdec-encoding=GB18030"}; //������Ļ����
//					frame.getMediaPlayer().playMedia("D:\\1\\Breakup.mkv",options);//һ����ֱ�Ӳ���
					
					
					frame.getMediaPlayer().prepareMedia("D:\\1\\Breakup.mkv",options);
					
					new SwingWorker<String, Integer>() {

						@Override
						protected String doInBackground() throws Exception {
							while (true) {			//ѭ�����½���ֵ
								long total = frame.getMediaPlayer().getLength();//��ȡ��ǰ��Ƶ�ĳ���  ����   ������
								long curr = frame.getMediaPlayer().getTime();	//��ȡ��ǰ���ŵĵ�
								float percent = (float)curr/total;
								publish((int)(percent*100));
								Thread.sleep(100);
							}
						}

						protected void process(java.util.List<Integer> chunks) {
							for (int v : chunks) {
								frame.getProgressBar().setValue(v);
							}

						};
					}.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	//����
	public static void play() {
		frame.getMediaPlayer().play();
	}

	//��ͣ
	public static void pause() {
		frame.getMediaPlayer().pause();
	}

	//ֹͣ
	public static void stop() {
		frame.getMediaPlayer().stop();
	}

	//��ת��
	public static void jumpTo(float to) {
		frame.getMediaPlayer().setTime((long)(to*frame.getMediaPlayer().getLength()));
	}
	
	//��������
	public static void setVol(int v) {
		frame.getMediaPlayer().setVolume(v);
	}
	
	
	//����Ƶ�ļ�
	public static void openVideo() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().playMedia(file.getAbsolutePath());
		}
	}

	//����Ļ�ļ�
	public static void openSubtitle() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().setSubTitleFile(file);
		}
	}
	
	//�˳�
	public static void exit() {
		frame.getMediaPlayer().release();
		System.exit(0);
	}
	
	
	

}
