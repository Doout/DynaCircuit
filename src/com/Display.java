package com;

import com.graphics.Renderable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/***
 * @author Baheer
 **/
public abstract class Display extends Canvas implements Runnable, Updatable, Renderable {

    /**
     *
     */
    private static final long serialVersionUID = 8145456036194879237L;
    public int WIDTH;
    public int HEIGHT;
    protected volatile boolean running;
    private Thread renderThread;

    /**
     * @param d The dimension of the frame
     **/
    public Display(Dimension d) {
        this((int) d.getWidth(), (int) d.getHeight());
    }

    /**
     * @param width  The width of the frame
     * @param height The height of the frame
     */
    public Display(int width, int height) {
        this.HEIGHT = height;
        this.WIDTH = width;
        final Dimension size = new Dimension((int) (WIDTH), (int) (HEIGHT));
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.setVisible(true);
        this.setFocusable(true);
        init();

    }

    /**
     * Create a frame in which the canvas can be render on
     *
     * @param dis  The Display in which to add to the frame
     * @param name The title of the frame (GUI)
     **/
    public static final JFrame CreateFrame(final Display dis, final String name,
                                           JMenuBar menubar) {
        final Display game = dis;
        final JFrame frame = new JFrame();
        // frame.setUndecorated(true);
        frame.setJMenuBar(menubar);
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setTitle(name);
        frame.setVisible(true);
        frame.setAlwaysOnTop(false);
        return frame;
    }

    public abstract void init();

    /**
     * Start the thread in which allow the screen to update
     */
    public void start() {
        if (this.running)
            return;
        this.renderThread = new Thread(this);
        this.running = true;
        this.renderThread.start();

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

    /**
     * The main method in which everything is render <br>
     * This method dont use the gpu.
     */
    public final void render() {
        final BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        final Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        render(g);
        g.dispose();
        bs.show();
    }

    @Override
    public final void run() {
        while (this.running) {
            update();
            render();
        }
    }

    /**
     * @param millis the length of time to sleep in milliseconds
     * @see {@link Thread#sleep(long)}
     **/
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

}