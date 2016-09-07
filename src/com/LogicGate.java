package com;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.array.BlockQueue;
import com.core.CoreSettings;
import com.core.DataCores;
import com.core.UpdateThread;
import com.core.impl.LogicCore;
import com.core.impl.PopUpMenuCore;
import com.core.impl.WireCore;
import com.graphics.Renderable;
import com.listener.LogicListener;
import com.logic.Logic;
import com.logic.LogicSettings;
import com.logic.controller.DEMUX;
import com.logic.controller.DFlipFlop;
import com.logic.controller.JKFlipFlop;
import com.logic.controller.MUX;
import com.logic.controller.SRFlipFlop;
import com.logic.controller.TFlipFlop;
import com.logic.gates.ANDGate;
import com.logic.gates.Buffer;
import com.logic.gates.NANDGate;
import com.logic.gates.NORGate;
import com.logic.gates.NOTGate;
import com.logic.gates.ORGate;
import com.logic.gates.XNORGate;
import com.logic.gates.XORGate;
import com.logic.input.Input;
import com.logic.input.Oscillators;
import com.logic.input.Switch;
import com.logic.output.LED;
import com.menu.ExpandibleMenu;
import com.menu.ExpandibleMenuList;
import com.menu.Menu;
import com.utils.Timer;
import com.utils.sprite.Sprite;
import com.wire.Wire;

public class LogicGate extends Display {

	// public static LogicGate instance;
	private static final long serialVersionUID = 848946808834860120L;
	public static final Object LOCK = new Object();
	public static String SAVE_LOCATION;

	public static int MENU_WIDTH = 182;
	private static LogicGate instance;

	private DataCores cores;

	private volatile Timer updateCheck;
	// If the program is not doing anything there is no point in render anything
	// or updating anything

	private ArrayList<Logic> logics;
	private ArrayList<Renderable> render;
	private ArrayList<Updatable> updatable;
	private ArrayList<Input> inputs;

	private Sprite backgound;

	private UpdateThread updateThread;
	private BlockQueue<Logic> updateQueue;
	private LogicMenuBar menubar;
	// private PopUpMenu rightclickmenu;

	// ZOOM var
	private Point renderOffSet;
	private double scaleX = 1.0, scaleY = 1.0;

	private Menu menu;

	private Timer cleanTimer;

	public LogicGate(int width, int height) {
		super(width, height);
	}

	private void initCores() { // should only be init once
		LogicCore.init();
		WireCore.init();
		PopUpMenuCore.init();
		LogicSettings.init();
		initSideMenu();

	}

	@Override
	public void init() {
		if (instance == null)
			instance = this;
		this.logics = new ArrayList<>(10);
		this.render = new ArrayList<>(2);
		this.updatable = new ArrayList<>(2);
		this.updateQueue = new BlockQueue<Logic>(1);
		this.inputs = new ArrayList<>();
		this.renderOffSet = new Point(0, 0);
		LogicListener ls = new LogicListener();
		this.addMouseListener(ls);
		this.addMouseMotionListener(ls);
		this.addMouseWheelListener(ls);
		this.addKeyListener(ls);
		cores = new DataCores(CoreSettings.MAX_SIZE_OF_CORES);
		cleanTimer = new Timer(1000);
		updateCheck = new Timer(2000);
		// Menu
		initCores();

		// Backgound
		backgound = Sprite.getSprite(LogicSettings.ID_BACKGROUND);
		backgound.getBounds().setBounds(0, 0, backgound.getBounds().width - MENU_WIDTH,
				backgound.getBounds().height);
		getCores().setMainProcess((Object... o) -> {
			updateCheck.reset();
			return null;
		});

	}

	@Override
	public void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(backgound.getImage(), MENU_WIDTH, 0, null);

		// Zoom start
		g.translate(renderOffSet.getX(), renderOffSet.getY());
		// g.scale(scaleX, scaleY);
		// g.clipRect(MENU_WIDTH, 0, WIDTH - MENU_WIDTH, HEIGHT);
		for (Renderable in : render) {
			in.render(g);
		}
		synchronized (LOCK) {
			for (Input in : inputs) {
				in.render(g);
			}
			for (Logic gate : logics) { // render everything but input
				gate.render(g);
			}
		}

		Wire.render(g);

		// Zoom end
		// g.scale(1 / scaleX, 1 / scaleY);
		g.translate(-renderOffSet.getX(), -renderOffSet.getY());
		g.setColor(Color.black);
		g.drawRect(0, 0, MENU_WIDTH, getHeight());

		// getMenuRightclick().render(g);
		menu.render(g);

		// This is just for debugging
		// System.out.println(System.currentTimeMillis() - startTime);
		// startTime = System.currentTimeMillis();
	}

	long startTime;

	@Override
	public void update() {
		while (!updateCheck.isRunning() && updateThread.isPause()) {
			sleep(200);
		}
		// callGarbageCollector();
		ArrayList<LogicState> u = new ArrayList<>(10);
		for (Logic g : logics) {
			for (LogicState in : g.getInputs()) {
				if (in == null)
					continue;
				if (in.isLocationUpdate()) {
					u.add(in);
					g.requestPathUpdate();
					break;
				}
			}
		} // remove all request to update the path
		for (LogicState in : u) {
			in.setLocationUpdate(false);
		}

		// update
		menu.update();
		synchronized (LOCK) {
			for (Updatable upd : updatable) {
				upd.update();
			}
		}
		// Let the cpu cool down
		sleep(33);
	}

	public void callGarbageCollector() {
		if (!cleanTimer.isRunning()) {
			cleanTimer.reset();
			System.gc();
		}
	}

	public BlockQueue<Logic> getUpdateQueue() {
		return updateQueue;
	}

	public ArrayList<Input> getInputs() {
		return inputs;
	}

	public ArrayList<Logic> getLogics() {
		return logics;
	}

	// public PopUpMenu getMenuRightclick() {
	// return rightclickmenu;
	// }

	public static LogicGate getInstance() {
		return instance;
	}

	public Rectangle findBounds(LogicState s) {
		for (Input in : getInputs()) {
			if (in.getOutput().equals(s))
				return (in.getBounds());
		}
		for (Logic in : getLogics()) {
			for (LogicState check : in.getOutputs())
				if (check.equals(s))
					return (in.getBounds());
		}
		return null;
	}

	/** return the point with the offset taking into account */
	public Point getRealPoint(Point point) {
		if (point.x < MENU_WIDTH)
			return point;
		return new Point((int) ((point.x - renderOffSet.x) / scaleX),
				(int) ((point.y - renderOffSet.y) / scaleY));

	}

	public void zoom(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public void start() {
		super.start();
		updateThread = new UpdateThread();
		updateThread.start();
	}

	public void removeLogic(Logic l) {
		synchronized (LOCK) {
			l.removeSelf();
			if (inputs.contains(l)) {
				inputs.remove(l);
			}
			if (logics.contains(l)) {
				logics.remove(l);
			}
		}
	}

	public String getFileSaveLocation(boolean save) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Logic File", "o");
		chooser.setFileFilter(filter);
		int returnVal = 0;
		if (save)
			chooser.showSaveDialog(null);
		else
			chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (chooser.getSelectedFile() == null)
				return null;
			if (chooser.getSelectedFile().getPath().endsWith(".o"))
				return chooser.getSelectedFile().getPath();
			else
				return chooser.getSelectedFile().getPath() + ".o";
		}
		return null;
	}

	public boolean save() {
		if (SAVE_LOCATION == null) {
			return false;
		}
		try {
			return save(new File(SAVE_LOCATION));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public byte[] getSaveBytes() throws IOException {
		return getSaveBytes(logics, inputs);
	}

	/** return the object in bytes form. */
	public byte[] getSaveBytes(Object... objs) throws IOException {
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(o);
		for (Object obj : objs) {
			out.writeObject(obj);

		}
		out.flush();
		// The only things we care about
		out.close();
		return o.toByteArray();
	}

	@SuppressWarnings("unused")
	public boolean save(File f) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(f);
		if (out == null)
			return false;
		out.write(getSaveBytes());
		out.close();
		return true;
	}

	public void load() {
		if (SAVE_LOCATION == null)
			return;
		File f = new File(SAVE_LOCATION);
		if (f.exists())
			try {
				loadFile(new File(SAVE_LOCATION));
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
	}

	@SuppressWarnings("unchecked")
	public void loadSave(byte[] byt) throws IOException, ClassNotFoundException {

		Object[] save = loadBytes(byt);
		LogicCore.removeAllSelect();

		logics = (ArrayList<Logic>) save[0];

		for (Logic l : logics) {
			l.setSprite(l.clone().getSprite());
			if (l.isSelect())
				LogicCore.selectLogics.add(l);

		}
		inputs = (ArrayList<Input>) save[1];

		for (Logic l : inputs) {
			l.setSprite(l.clone().getSprite());
			if (l.isSelect())
				LogicCore.selectLogics.add(l);
		}

	}

	@SuppressWarnings("unchecked")
	public static Object[] loadBytes(byte[] byt) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream i = new ByteArrayInputStream(byt);
		ObjectInputStream in = new ObjectInputStream(i);
		ArrayList<Object> objs = new ArrayList<>(2);
		Object j;
		if (in != null) {
			try {
				while ((j = in.readObject()) != null) {
					objs.add(j);
				}
			} catch (EOFException e) {
			}

		}
		return objs.toArray();
	}

	@SuppressWarnings("unchecked")
	public void loadFile(File f) throws FileNotFoundException, IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		if (in != null) {
			logics = (ArrayList<Logic>) in.readObject();
			for (Logic l : logics) {
				l.setSprite(l.clone().getSprite());
			}
			inputs = (ArrayList<Input>) in.readObject();
			for (Logic l : inputs) {
				l.setSprite(l.clone().getSprite());
				l.update();
				getUpdateQueue().push(l);
			}
			in.close();
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			ClassNotFoundException, IOException {

		LogicGate log = new LogicGate(900, 600);
		LogicGate.instance = log;
		JFrame frame = Display.CreateFrame(log, "Logic Gates", new LogicMenuBar());
		log.menubar = (LogicMenuBar) frame.getJMenuBar();
		log.start();
  
	}

	public void initSideMenu() {

		menu = new ExpandibleMenuList(HEIGHT - 200);
		menu.addListener();
		((ExpandibleMenuList) menu).setMoveable(true);

		ExpandibleMenu menu1 = new ExpandibleMenu("Gates", 2, 20, 10, MENU_WIDTH);
		menu1.setProcess(LogicCore.menuProcess); // Without this drag will not
													// work
		menu1.add("AND", new ANDGate(2));
		menu1.add("OR", new ORGate(2));
		menu1.add("NOT", new NOTGate());
		menu1.add("BUFFER", new Buffer());
		menu1.add("NAND", new NANDGate(2));
		menu1.add("NOR", new NORGate(2));
		menu1.add("XOR", new XORGate(2));
		menu1.add("XNOR", new XNORGate(2));

		((ExpandibleMenuList) menu).add(menu1);

		menu1 = new ExpandibleMenu("Inputs & Outputs", 2, 20, 10, MENU_WIDTH);
		menu1.setProcess(LogicCore.menuProcess);
		menu1.add("Switch", new Switch());
		menu1.add("Oscillators", new Oscillators(-1)); // remove from update
														// list
		menu1.add("LED", new LED());

		((ExpandibleMenuList) menu).add(menu1);

		menu1 = new ExpandibleMenu("Controllers", 2, 20, 15, MENU_WIDTH);
		menu1.setProcess(LogicCore.menuProcess);
		menu1.add("Mux", new MUX(1));
		menu1.add("Demux", new DEMUX(1));
		menu1.add("Encoder", null);
		menu1.add("Decoder", null);
		menu1.add("SR flip-flop", new SRFlipFlop());
		menu1.add("JK flip-flop", new JKFlipFlop());
		menu1.add("D flip-flop", new DFlipFlop());
		menu1.add("T flip-flop", new TFlipFlop());

		((ExpandibleMenuList) menu).add(menu1);
	}

	public Menu getMenu() {
		return menu;
	}

	public ArrayList<Renderable> getRenderList() {
		return render;
	}

	public ArrayList<Updatable> getUpdatable() {
		return updatable;
	}

	public DataCores getCores() {
		return cores;
	}

	public LogicMenuBar getMenubar() {
		return menubar;
	}
}
