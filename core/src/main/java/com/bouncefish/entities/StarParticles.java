package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bouncefish.utils.GameConstants;

public class StarParticles {
    private static final int PARTICLES_PER_BURST = 3;
    private static final float LIFETIME = 0.55f;
    private static final float GRAVITY = 500f;
    private static final float MIN_SPEED = 260f;
    private static final float MAX_SPEED = 480f;
    private static final float BASE_SIZE = 22f;
    private static final float SIZE_JITTER = 8f;

    private static Texture pixel;
    private static TextureRegion pixelRegion;
    private static final Array<Particle> particles = new Array<>();

    private static class Particle {
        float x, y;
        float vx, vy;
        float life;
        float rot;
        float rotSpeed;
        float size;
    }

    public static void init() {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1, 1, 1, 1);
        pm.fill();
        pixel = new Texture(pm);
        pixelRegion = new TextureRegion(pixel);
        pm.dispose();
    }

    public static void spawn(float x, float y) {
        if (!GameConstants.STAR_PARTICLES_ENABLED) return;
        for (int i = 0; i < PARTICLES_PER_BURST; i++) {
            Particle p = new Particle();
            p.x = x;
            p.y = y;
            float angle = (float)(Math.PI * 0.25 + Math.random() * Math.PI * 0.5);
            float speed = MIN_SPEED + (float)Math.random() * (MAX_SPEED - MIN_SPEED);
            p.vx = (float)Math.cos(angle) * speed;
            p.vy = (float)Math.sin(angle) * speed;
            p.life = LIFETIME;
            p.rot = (float)(Math.random() * 360f);
            p.rotSpeed = ((float)Math.random() - 0.5f) * 720f;
            p.size = BASE_SIZE + (float)Math.random() * SIZE_JITTER;
            particles.add(p);
        }
    }

    public static void update(float delta) {
        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.life -= delta;
            if (p.life <= 0) {
                particles.removeIndex(i);
                continue;
            }
            p.x += p.vx * delta;
            p.y += p.vy * delta;
            p.vy -= GRAVITY * delta;
            p.rot += p.rotSpeed * delta;
        }
    }

    public static void render(SpriteBatch batch) {
        if (pixel == null || particles.size == 0) return;
        for (int i = 0; i < particles.size; i++) {
            Particle p = particles.get(i);
            float lifeFrac = p.life / LIFETIME;
            float alpha = Math.min(1f, lifeFrac * 1.8f);
            float thickness = p.size * 0.22f;
            batch.setColor(1f, 0.92f, 0.28f, alpha);
            batch.draw(pixelRegion, p.x - p.size / 2f, p.y - thickness / 2f, p.size / 2f, thickness / 2f, p.size, thickness, 1f, 1f, p.rot);
            batch.draw(pixelRegion, p.x - thickness / 2f, p.y - p.size / 2f, thickness / 2f, p.size / 2f, thickness, p.size, 1f, 1f, p.rot);
            batch.setColor(1f, 1f, 0.7f, alpha);
            float coreSize = p.size * 0.35f;
            batch.draw(pixel, p.x - coreSize / 2f, p.y - coreSize / 2f, coreSize, coreSize);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public static void dispose() {
        if (pixel != null) {
            pixel.dispose();
            pixel = null;
            pixelRegion = null;
        }
        particles.clear();
    }
}
