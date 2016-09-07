package com.core;

import com.LogicGate;
import com.LogicState;
import com.logic.Logic;
import com.utils.Timer;

import java.lang.management.ManagementFactory;

import javax.management.*;

/***
 * @author Baheer
 **/
public class UpdateThread implements Runnable {

	private volatile boolean running;
	private volatile boolean pause;
	private volatile long lastUpdate;
	private Timer timer = new Timer(20);
	private Thread renderThread;

	@Override
	public final void run() {
		Logic key;
		Timer cpuCheck = new Timer(5000);
		double cpuload;
		while (this.running) {
			while (pause)
				sleep(500);
			while (timer.isRunning())
				sleep(timer.getRemaining());
			while (!LogicGate.getInstance().getUpdateQueue().isEmpty()) {
				lastUpdate = System.currentTimeMillis();
			//	System.out.println(LogicGate.getInstance().getUpdateQueue().size());
				if (!cpuCheck.isRunning()) {
					cpuload = getProcessCpuLoad();
					if (cpuload < 30)
						cpuCheck.reset();
					else { // if load is more than 30% than sleep
						sleep(5);
					}
				}
				key = LogicGate.getInstance().getUpdateQueue().pop();
				if (key == null)
					continue;
				key.update();
				for (LogicState out : key.getOutputs()) {
					if (out.stateUpdate())
						for (Logic link : out.getLogicInput()) {
							// If null then the list got remade
							if (link == null)
								break;
							// Stop going from this path if the state of the
							// output did not change
							if (link.canUpdate()) // flop
								LogicGate.getInstance().getUpdateQueue().push(link);
						}
				}
			}
			if ((System.currentTimeMillis() - lastUpdate) > 2000)
				pause = true;
			timer.reset();
		}
	}

	public static double getProcessCpuLoad() {
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
			AttributeList list = mbs.getAttributes(name,
					new String[] { "ProcessCpuLoad" });
			if (list.isEmpty())
				return Double.NaN;
			Attribute att = (Attribute) list.get(0);
			Double value = (Double) att.getValue();
			if (value == -1.0)
				return Double.NaN;
			return ((int) (value * 1000) / 10.0);

		} catch (Exception e) {
			return 0;
		}
	}

	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/** Start the thread */
	public void start() {
		if (this.running)
			return;
		this.renderThread = new Thread(this);
		this.running = true;
		this.renderThread.start();
		LogicGate.getInstance().getCores().add(CoreSettings.MOUSE_CLICKED,
				new ProcessData((Object... o) -> {
					setPause(false);
					return null;
				}));

	}

	/**
	 * Stop the thread from running. After this method is call the screen will
	 * not update anymore
	 **/
	public void Stop() {
		if (!this.running) {
			return;
		}
		this.running = false;
		try {
			this.renderThread.join(500);
		} catch (final InterruptedException e) {
			System.exit(0); // no chill fam, none at all
		}
	}
}