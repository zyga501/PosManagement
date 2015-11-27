package com.posmanagement.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

public class VerifyCode {
    public  VerifyCode(int codeLength) {
        codeLenth_ = codeLength;
        if (codeLength <= 0)
            throw new IllegalArgumentException();
    }

    public String generateImage(int width, int height, OutputStream imageStream) throws IOException {
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW };
        Random rand = new Random();
        float[] fractions = new float[colors.length];
        for(int i = 0; i < colors.length; i++){
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        BufferedImage imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = imageBuffer.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.setColor(Color.GRAY);// 设置边框色
        graphics2D.fillRect(0, 0, width, height);

        Color color = new Color(generateRandomColor(200, 250), false);
        graphics2D.setColor(color);// 设置背景色
        graphics2D.fillRect(0, 2, width, height - 4);

        //绘制干扰线
        Random random = new Random();
        graphics2D.setColor(new Color(generateRandomColor(160, 200), false));// 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            int left = random.nextInt(width - 1);
            int top = random.nextInt(height - 1);
            int _width = random.nextInt(6) + 40;
            int _height = random.nextInt(12) + 20;
            graphics2D.drawLine(left, top, left + _width, top + _height);
        }

        // 添加噪点
        float yawpRate = 0.05f;// 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            imageBuffer.setRGB(random.nextInt(width), random.nextInt(height), generateRandomColor(0, 255));
        }

        shearArea(graphics2D, width, height, color);

        graphics2D.setColor(new Color(generateRandomColor(100, 160), false));
        int fontSize = height - 4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        graphics2D.setFont(font);

        String randomString = generateRandomString();
        char[] chars = randomString.toCharArray();
        for(int i = 0; i < codeLenth_; i++){
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (width / codeLenth_) * i + fontSize/2, height / 2);
            graphics2D.setTransform(affine);
            graphics2D.drawChars(chars, i, 1, ((width - 10) / codeLenth_) * i + 5, height / 2 + fontSize/2 - 10);
        }

        graphics2D.dispose();
        ImageIO.write(imageBuffer, "jpg", imageStream);
        return randomString;
    }

    private String generateRandomString() {
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder randomString = new StringBuilder(codeLenth_);
        for(int i = 0; i < codeLenth_; i++){
            randomString.append(VERIFY_CODES.charAt(rand.nextInt(codeLenth_ - 1)));
        }
        return randomString.toString();
    }

    private int generateRandomColor(int minRate, int maxRate) {
        maxRate = maxRate % 256;
        minRate = minRate % 256;
        int rate = maxRate - minRate;
        return (minRate + random.nextInt(rate)) << 16
                | (minRate + random.nextInt(rate)) << 8
                | (minRate + random.nextInt(rate));
    }

    private void shearArea(Graphics g, int w1, int h1, Color color) {
        shearWidth(g, w1, h1, color);
        shearHight(g, w1, h1, color);
    }

    private void shearWidth(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
        for (int index = 0; index < h1; index++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) index / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, index, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, index, 0, index);
                g.drawLine((int) d + w1, index, w1, index);
            }
        }
    }

    private void shearHight(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10; // 50;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }

    private static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static Random random = new Random();
    private int codeLenth_;
}
