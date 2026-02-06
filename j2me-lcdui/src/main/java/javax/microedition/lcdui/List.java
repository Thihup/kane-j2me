package javax.microedition.lcdui;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.util.Vector;

public class List extends Screen implements Choice {
    public static final int IMPLICIT = 3;
    public static final int EXCLUSIVE = 1;
    public static final int MULTIPLE = 2;

    private int listType;
    private final Vector<String> strings = new Vector<>();
    private final Vector<Image> images = new Vector<>();
    private final JList<String> swingList;
    private CommandListener listener;

    public List(String title, int listType) {
        super();
        setTitle(title);
        this.listType = listType;

        swingList = new JList<>();
        // Basic mapping of types
        if (listType == MULTIPLE) {
            swingList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            swingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        // Add to the screen panel
        _swingPanel.setLayout(new BorderLayout());
        _swingPanel.add(new JScrollPane(swingList), BorderLayout.CENTER);
        
        // Handle selection events to trigger commands if IMPLICIT
        swingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listType == IMPLICIT && listener != null) {
                // In J2ME, implicit lists fire a special command.
                // We'd need to know what the "Select" command is.
                // For now, we just acknowledge the selection.
            }
        });
    }

    public List(String title, int listType, String[] stringElements, Image[] imageElements) {
        this(title, listType);
        if (stringElements != null) {
            for (int i = 0; i < stringElements.length; i++) {
                append(stringElements[i], imageElements != null && i < imageElements.length ? imageElements[i] : null);
            }
        }
    }

    public int append(String stringPart, Image imagePart) {
        strings.add(stringPart);
        images.add(imagePart);
        swingList.setListData(strings); // inefficient but simple
        return strings.size() - 1;
    }

    public void delete(int elementNum) {
        strings.remove(elementNum);
        images.remove(elementNum);
        swingList.setListData(strings);
    }

    public void deleteAll() {
        strings.clear();
        images.clear();
        swingList.setListData(strings);
    }

    public int size() {
        return strings.size();
    }

    // --- Choice Interface Methods ---
    // (Partial implementation for now)
    
    public int getSelectedIndex() {
        return swingList.getSelectedIndex();
    }
    
    public void setSelectedIndex(int index, boolean selected) {
        swingList.setSelectedIndex(index);
    }

    @Override
    public void setCommandListener(CommandListener l) {
        super.setCommandListener(l);
        this.listener = l;
    }
}
