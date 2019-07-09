package Shape;

public class Colour {
    private int r;
    private int g;
    private int b;

    public Colour(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Colour(){
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return ("(" + this.r + "," + this.g + "," + this.b + ")");
    }
}
