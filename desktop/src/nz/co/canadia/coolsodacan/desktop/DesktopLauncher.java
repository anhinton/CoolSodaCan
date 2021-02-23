package nz.co.canadia.coolsodacan.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import nz.co.canadia.coolsodacan.Constants;
import nz.co.canadia.coolsodacan.CoolSodaCan;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(Constants.GAME_NAME);
		config.setWindowedMode(Constants.DESKTOP_WIDTH, Constants.DESKTOP_HEIGHT);

		new Lwjgl3Application(new CoolSodaCan(), config);
	}
}
