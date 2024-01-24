package data;

public class Coordinates {

    private final String x;
    private final String y;
    private final String r;

    public Coordinates(String x, String y, String r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public String toString() {
        return String.format("(%s; %s; %s)", x, y, r);
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getR() {
        return r;
    }
}
