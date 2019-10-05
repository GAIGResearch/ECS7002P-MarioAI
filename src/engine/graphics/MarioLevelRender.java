package engine.graphics;

import engine.core.MarioWorld;
import engine.helper.Assets;

import java.awt.*;

public class MarioLevelRender extends MarioRender {
    private float scale;
    private MarioWorld worldCache = null;

    public MarioLevelRender(float scale, int width, int height) {
        this.setEnabled(true);
        this.scale = scale;

        Dimension size = new Dimension((int) (width * scale), (int) (height * scale));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }
    public void init() {
        Assets.init(getGraphicsConfiguration());
    }

    public void renderWorld(MarioWorld world) {
        this.worldCache = world;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (worldCache != null) {
            worldCache.renderFull(g, scale);
        }
    }
}
