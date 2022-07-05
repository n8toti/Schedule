
package sample;
/**
 * total class was created to get the type and count of each type and present it into a GUI.
 */
public class total {
    private int count;
    private String type;

    public total(int count, String type) {
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
