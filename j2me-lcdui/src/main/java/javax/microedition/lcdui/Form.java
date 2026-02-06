package javax.microedition.lcdui;

import java.util.ArrayList;
import java.util.List;

public class Form extends Screen {
    private List<Item> items = new ArrayList<>();

    public Form(String title) {
        super();
        setTitle(title);
    }

    public Form(String title, Item[] items) {
        this(title);
        if (items != null) {
            for (Item item : items) {
                append(item);
            }
        }
    }

    public int append(Item item) {
        items.add(item);
        _swingPanel.add(item.getSwingComponent());
        _swingPanel.revalidate();
        return items.size() - 1;
    }

    public int append(String str) {
        return append(new StringItem(null, str));
    }

    public void deleteAll() {
        items.clear();
        _swingPanel.removeAll();
        _swingPanel.revalidate();
        _swingPanel.repaint();
    }
}
