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
					RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"		//Mac系统下
					);
		}else if (RuntimeUtil.isWindows()) {
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "F:\\VLC_Player\\VideoLAN\\VLC"			//Window系统下F:\VLC_Player\VLC\sdk\lib\libvlc.lib
					);
		}else if (RuntimeUtil.isNix()) {
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib"			//Linux系统下
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
					
					String options[] = {"--subsdec-encoding=GB18030"}; //设置字幕编码
//					frame.getMediaPlayer().playMedia("D:\\1\\Breakup.mkv",options);//一运行直接播放
					
					
					frame.getMediaPlayer().prepareMedia("D:\\1\\Breakup.mkv",options);
					
					new SwingWorker<String, Integer>() {

						@Override
						protected String doInBackground() throws Exception {
							while (true) {			//循环更新进度值
								long total = frame.getMediaPlayer().getLength();//获取当前视频的长度  毫秒   长整型
								long curr = frame.getMediaPlayer().getTime();	//获取当前播放的点
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
	
	//播放
	public static void play() {
		frame.getMediaPlayer().play();
	}

	//暂停
	public static void pause() {
		frame.getMediaPlayer().pause();
	}

	//停止
	public static void stop() {
		frame.getMediaPlayer().stop();
	}

	//跳转到
	public static void jumpTo(float to) {
		frame.getMediaPlayer().setTime((long)(to*frame.getMediaPlayer().getLength()));
	}
	
	//设置音量
	public static void setVol(int v) {
		frame.getMediaPlayer().setVolume(v);
	}
	
	
	//打开视频文件
	public static void openVideo() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().playMedia(file.getAbsolutePath());
		}
	}

	//打开字幕文件
	public static void openSubtitle() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().setSubTitleFile(file);
		}
	}
	
	//退出
	public static void exit() {
		frame.getMediaPlayer().release();
		System.exit(0);
	}
	
	
	

}
