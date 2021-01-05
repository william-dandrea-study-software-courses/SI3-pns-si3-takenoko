package fr.matelots.polytech.engine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineDrawer {

    private static float lerp(int a, int b, float t) {
        return a + (float)(b - a) * t;
    }

    private static Position lerp(Position a, Position b, float t) {
        var cubex = lerp(a.getX(), b.getX(), t);
        var cubey = lerp(a.getY(), b.getY(), t);
        var cubez = lerp(a.getZ(), b.getZ(), t);



        int rx = Math.round(cubex);
        int ry = Math.round(cubey);
        int rz = Math.round(cubez);

        float x_diff = Math.abs(cubex - rx);
        float y_diff = Math.abs(cubey - ry);
        float z_diff = Math.abs(cubez - rz);

        if(x_diff > y_diff && x_diff > z_diff) {
            rx = -ry -rz;
        } else if(y_diff > z_diff) {
            ry = -rx-rz;
        } else {
            rz = -rx-ry;
        }

        return new Position(rx, ry, rz);
    }

    private static int cubeDistance(Position a, Position b) {
        return (Math.abs(b.getX() - a.getX()) +
                Math.abs(b.getY() - a.getY()) +
                Math.abs(b.getZ() - a.getZ())) / 2;
    }

    public static List<Position> getLine(Position a, Position b) {
        var pathLength = cubeDistance(a, b);
        var result = new ArrayList<Position>();

        for(int i = 0; i <= pathLength; i++) {
            result.add(lerp(a, b, (float) ((float)i / (float)pathLength)));
        }

        return result;
    }
}
