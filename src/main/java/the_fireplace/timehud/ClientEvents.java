package the_fireplace.timehud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import the_fireplace.timehud.config.ConfigValues;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ClientEvents {
	@SubscribeEvent
	public void guiRender(TickEvent.RenderTickEvent t){
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.inGameHasFocus){
			if(ConfigValues.NEEDCLOCK && !hasClock())
				return;
			ScaledResolution res = new ScaledResolution(mc);
			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			int x=0, y=0;
			String[] loc = ConfigValues.LOCATION.split("-");
			if(loc[0].equals("top"))
				y=4;
			if(loc[0].equals("center"))
				y=height/2-6;
			if(loc[0].equals("bottom"))
				y=height-4-12;
			if(loc[1].equals("left"))
				x=4;
			if(loc[1].equals("center"))
				x=width/2-25;
			if(loc[1].equals("right"))
				x=width-4-50;
			String d2 = ConfigValues.FORMAT;
			if(ConfigValues.REAL){
				if(d2.contains("MONTH"))
					d2 = d2.replace("MONTH", String.valueOf(Calendar.getInstance().get(Calendar.MONTH)));
				if(d2.contains("DATE"))
					d2 = d2.replace("DATE", String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
				if(d2.contains("YEAR"))
					d2 = d2.replace("YEAR", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
				if(d2.contains("12HH")) {
					int hour = Calendar.getInstance().get(Calendar.HOUR);
					if(hour == 0)
						hour = 12;
					d2 = d2.replace("12HH", String.valueOf(hour));
				}
				if(d2.contains("24HH")) {
					d2 = d2.replace("24HH", String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
				}
				if(d2.contains("MM"))
					d2 = d2.replace("MM", String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)));
				if(d2.contains("SS"))
					d2 = d2.replace("SS", String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));
				if(d2.contains("NAME"))
					d2 = d2.replace("NAME", getMonthForInt(Calendar.getInstance().get(Calendar.MONTH)));
				if(d2.contains("ZZ")) {
					if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 11)
						d2 = d2.replace("ZZ", "PM");
					else
						d2 = d2.replace("ZZ", "AM");
				}
			}else{
				long month = 1, day, year = 1;
				long hour = 6, minute = 0, second = 0;
				long daylength = 24000;
				long worldtime = mc.world.getWorldTime();
				long daycount = (long)Math.floor(worldtime/daylength);
				long remainingticks = worldtime%daylength;
				String[] names = new String[]{I18n.format("january"), I18n.format("february"), I18n.format("march"), I18n.format("april"), I18n.format("may"), I18n.format("june"), I18n.format("july"), I18n.format("august"), I18n.format("september"), I18n.format("october"), I18n.format("november"), I18n.format("december")};
				while(daycount > 365){
					daycount -= 365;
					year++;
				}
				if(daycount > 31){
					daycount -= 31;
					month++;
				}
				if(daycount > 28){
					daycount -= 28;
					month++;
				}
				if(daycount > 31){
					daycount -= 31;
					month++;
				}
				if(daycount > 30){
					daycount -= 30;
					month++;
				}
				if(daycount > 31){
					daycount -= 31;
					month++;
				}
				if(daycount > 30){
					daycount -= 30;
					month++;
				}
				if(daycount > 31){
					daycount -= 31;
					month++;
				}
				if(daycount > 31){
					daycount -= 31;
					month++;
				}
				if(daycount > 30){
					daycount -= 30;
					month++;
				}
				if(daycount > 31){
					daycount -= 31;
					month++;
				}
				if(daycount > 30){
					daycount -= 30;
					month++;
				}
				day = daycount+1;

				while(remainingticks >= 1000){
					remainingticks -= 1000;
					hour++;
					if(hour > 24)
						hour -= 24;
				}
				remainingticks *= 3;//60 ticks per second, allows for even division into minutes
				while (remainingticks >= 50){
					remainingticks -= 50;
					minute++;
				}
				remainingticks *= 6;//360 ticks per second, allows for division into seconds
				while(remainingticks >= 5){
					remainingticks -= 5;
					second++;
				}

				if(d2.contains("MONTH"))
					d2 = d2.replace("MONTH", String.valueOf(month));
				if(d2.contains("DATE"))
					d2 = d2.replace("DATE", String.valueOf(day));
				if(d2.contains("YEAR"))
					d2 = d2.replace("YEAR", String.valueOf(year));
				if(d2.contains("12HH")) {
					long hour2 = hour;
					if(hour2 > 12)
						hour2 -= 12;
					d2 = d2.replace("12HH", String.valueOf(hour2));
				}
				if(d2.contains("24HH")) {
					d2 = d2.replace("24HH", String.valueOf(hour));
				}
				if(d2.contains("MM")) {
					String m = String.valueOf(minute);
					if(m.length() == 1)
						d2 = d2.replace("MM", "0"+m);
					else
						d2 = d2.replace("MM", m);
				}
				if(d2.contains("SS")) {
					String s = String.valueOf(second);
					if(s.length() == 1)
						d2 = d2.replace("SS", "0"+s);
					else
						d2 = d2.replace("SS", s);
				}
				if(d2.contains("NAME"))
					d2 = d2.replace("NAME", names[(int)month-1]);
				if(d2.contains("ZZ")) {
					if(hour > 11 && hour < 24)
						d2 = d2.replace("ZZ", "PM");
					else
						d2 = d2.replace("ZZ", "AM");
				}
			}
			String[] d3 = d2.split("BR");
			mc.ingameGUI.drawString(Minecraft.getMinecraft().fontRendererObj, d3[0], x, y, -1);
			if(d3.length > 1)
				mc.ingameGUI.drawString(Minecraft.getMinecraft().fontRendererObj, d3[1], x, y+12, -1);
			}
		}
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if(eventArgs.getModID().equals(TimeHud.MODID))
			TimeHud.syncConfig();
	}
	private boolean hasClock(){
		for(ItemStack stack:Minecraft.getMinecraft().player.inventory.mainInventory)
			if(stack != null)
				if(stack.getItem().equals(Items.CLOCK))
					return true;
		return false;
	}
	private String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11 ) {
			month = months[num];
		}
		return month;
	}
}