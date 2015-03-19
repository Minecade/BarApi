package me.confuser.barapi.nms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.confuser.barapi.Util;

import org.bukkit.Location;

/**
* This is the FakeDragon class for BarAPI.
* It is based on the code by SoThatsIt.
*
* http://forums.bukkit.org/threads/tutorial-utilizing-the-boss-health-bar.158018/page-5#post-2053705
*
* @author James Mortemore
*/

public class v1_8Fake extends FakeDragon {
	private Object wither;
	private int id;

	public v1_8Fake(String name, Location loc) {
		super(name, loc);
	}

	@Override
	public Object getSpawnPacket() {
		Class<?> Entity = Util.getCraftClass("Entity");
		Class<?> EntityLiving = Util.getCraftClass("EntityLiving");
		//Class<?> EntityEnderDragon = Util.getCraftClass("EntityEnderDragon");
		Class<?> EntityWither = Util.getCraftClass("EntityWither");
		Object packet = null;
		try {
			wither = EntityWither.getConstructor(Util.getCraftClass("World")).newInstance(getWorld());

			Method setLocation = Util.getMethod(EntityWither, "setLocation", new Class<?>[] { double.class, double.class, double.class, float.class, float.class });
			setLocation.invoke(wither, getX(), getY(), getZ(), getPitch(), getYaw());

			Method setInvisible = Util.getMethod(EntityWither, "setInvisible", new Class<?>[] { boolean.class });
			setInvisible.invoke(wither, true);

			Method setCustomName = Util.getMethod(EntityWither, "setCustomName", new Class<?>[] { String.class });
			setCustomName.invoke(wither, name);

			Method setHealth = Util.getMethod(EntityWither, "setHealth", new Class<?>[] { float.class });
			setHealth.invoke(wither, health);

			Field motX = Util.getField(Entity, "motX");
			motX.set(wither, getXvel());

			Field motY = Util.getField(Entity, "motY");
			motY.set(wither, getYvel());

			Field motZ = Util.getField(Entity, "motZ");
			motZ.set(wither, getZvel());

			Method getId = Util.getMethod(EntityWither, "getId", new Class<?>[] {});
			this.id = (Integer) getId.invoke(wither);

			Class<?> PacketPlayOutSpawnEntityLiving = Util.getCraftClass("PacketPlayOutSpawnEntityLiving");

			packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class<?>[] { EntityLiving }).newInstance(wither);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return packet;
	}

	@Override
	public Object getDestroyPacket() {
		Class<?> PacketPlayOutEntityDestroy = Util.getCraftClass("PacketPlayOutEntityDestroy");

		Object packet = null;
		try {
			packet = PacketPlayOutEntityDestroy.newInstance();
			Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
			a.setAccessible(true);
			a.set(packet, new int[] { id });
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return packet;
	}

	@Override
	public Object getMetaPacket(Object watcher) {
		Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

		Class<?> PacketPlayOutEntityMetadata = Util.getCraftClass("PacketPlayOutEntityMetadata");

		Object packet = null;
		try {
			packet = PacketPlayOutEntityMetadata.getConstructor(new Class<?>[] { int.class, DataWatcher, boolean.class }).newInstance(id, watcher, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return packet;
	}

	@Override
	public Object getTeleportPacket(Location loc) {
		Class<?> PacketPlayOutEntityTeleport = Util.getCraftClass("PacketPlayOutEntityTeleport");
		Object packet = null;

		try {
			packet = PacketPlayOutEntityTeleport.getConstructor(new Class<?>[] { int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class }).newInstance(this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360), false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return packet;
	}

	@Override
	public Object getWatcher() {
		Class<?> Entity = Util.getCraftClass("Entity");
		Class<?> DataWatcher = Util.getCraftClass("DataWatcher");

		Object watcher = null;
		try {
			watcher = DataWatcher.getConstructor(new Class<?>[] { Entity }).newInstance(wither);
			Method a = Util.getMethod(DataWatcher, "a", new Class<?>[] { int.class, Object.class });

			a.invoke(watcher, 5, isVisible() ? (byte) 0 : (byte) 0x20);
			a.invoke(watcher, 6, (Float) health);
			a.invoke(watcher, 7, (Integer) 0);
			a.invoke(watcher, 8, (Byte) (byte) 0);
			a.invoke(watcher, 10, name);
			a.invoke(watcher, 11, (Byte) (byte) 1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return watcher;
	}
}
