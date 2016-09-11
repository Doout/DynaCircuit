package com.graphics;

import com.sun.istack.internal.NotNull;
import java.awt.*;

/**
 * @author Baheer Kamal
 ***/
public interface Renderable {

     void render(@NotNull Graphics2D g);
}
