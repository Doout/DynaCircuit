package com.logic;

import com.utils.sprite.Sprite;

import java.util.HashMap;

public class LogicSettings {

    public static int SIZE = 0;
    public static final int ID_AND = SIZE++;
    public static final int ID_OR = SIZE++;
    public static final int ID_NOT = SIZE++;
    public static final int ID_BUFFER = SIZE++;
    public static final int ID_XOR = SIZE++;
    public static final int ID_NAND = SIZE++;
    public static final int ID_NOR = SIZE++;
    public static final int ID_XNOR = SIZE++;
    public static final int ID_SWITCH_ON = SIZE++;
    public static final int ID_SWITCH_OFF = SIZE++;
    public static final int ID_LED_ON = SIZE++;
    public static final int ID_LED_OFF = SIZE++;
    public static final int ID_MUX = SIZE++;
    public static final int ID_DEMUX = SIZE++;
    public static final int ID_JKFLIPFLOP = SIZE++;
    public static final int ID_SRFLIPFLOP = SIZE++;
    public static final int ID_TFLIPFLOP = SIZE++;
    public static final int ID_DFLIPFLOP = SIZE++;
    public static final int ID_BACKGROUND = SIZE++;
    public static final int ID_OSCILLATORS = SIZE++;

    public static void init() {
        HashMap<Integer, String> data = new HashMap<>();
        data.put(ID_AND, "and.png");
        data.put(ID_OR, "or.png");
        data.put(ID_NOT, "not.png");
        data.put(ID_BUFFER, "buffer.png");
        data.put(ID_XOR, "xor.png");
        data.put(ID_NAND, "nand.png");
        data.put(ID_NOR, "nor.png");
        data.put(ID_XNOR, "xnor.png");
        data.put(ID_SWITCH_ON, "switch_on.png");
        data.put(ID_SWITCH_OFF, "switch_off.png");
        data.put(ID_LED_ON, "led_on.png");
        data.put(ID_LED_OFF, "led_off.png");
        data.put(ID_MUX, "mux.png");
        data.put(ID_DEMUX, "demux.png");
        data.put(ID_BACKGROUND, "backgound.png");
        data.put(ID_JKFLIPFLOP, "jkflipflop.png");
        data.put(ID_SRFLIPFLOP, "srflipflop.png");
        data.put(ID_TFLIPFLOP, "tflipflop.png");
        data.put(ID_DFLIPFLOP, "dflipflop.png");
        data.put(ID_OSCILLATORS, "oscillators.png");
        Sprite.setIDList(data);
    }

    private LogicSettings() {

    }

}
