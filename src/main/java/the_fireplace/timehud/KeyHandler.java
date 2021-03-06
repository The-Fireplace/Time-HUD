package the_fireplace.timehud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import the_fireplace.timehud.gui.GuiChooseColor;
import the_fireplace.timehud.gui.GuiClockMoving;

/**
 * @author The_Fireplace
 */
public class KeyHandler {
	//Key Index
	public static final int MOVECLOCK = 0;
	public static final int PICKCOLOR = 1;
	//Descriptions, use language file to localize later
	private static final String[] desc =
			{"key.moveclock.desc", "key.pickcolor.desc"};
	//Default Key Values
	private static final int[] keyValues =
			{Keyboard.KEY_MINUS, Keyboard.KEY_BACKSLASH};
	private final KeyBinding[] keys;
	public KeyHandler(){
		keys = new KeyBinding[desc.length];
		for(int i = 0; i < desc.length; ++i){
			keys[i] = new KeyBinding(desc[i], keyValues[i], "key.timehud.category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event){
		if(keys[MOVECLOCK].isPressed()){
			Minecraft.getMinecraft().displayGuiScreen(new GuiClockMoving());
		}
		if(keys[PICKCOLOR].isPressed()){
			Minecraft.getMinecraft().displayGuiScreen(new GuiChooseColor());
		}
	}

	public int getKeyCode(int keyBind){
		return keys[keyBind].getKeyCode();
	}
}