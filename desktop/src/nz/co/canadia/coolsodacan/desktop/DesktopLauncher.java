package nz.co.canadia.coolsodacan.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import nz.co.canadia.coolsodacan.Constants;
import nz.co.canadia.coolsodacan.CoolSodaCan;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(Constants.GAME_NAME);
		config.setWindowIcon("desktopIcons/icon_128.png",
				"desktopIcons/icon_32.png",
				"desktopIcons/icon_16.png");
		config.setWindowedMode(Constants.DESKTOP_WIDTH, Constants.DESKTOP_HEIGHT);
		config.setResizable(false);

		new Lwjgl3Application(new CoolSodaCan(new DesktopFontLoader(), new DesktopFormatter()), config);
	}
}
