package javax.microedition.lcdui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class ChoiceGroup extends Item implements Choice {
    private int choiceType;
    private List<String> strings = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    private int selectedIndex = -1;

    public ChoiceGroup(String label, int choiceType) {
        super(label);
        this.choiceType = choiceType;
    }

    public ChoiceGroup(String label, int choiceType, String[] stringElements, Image[] imageElements) {
        this(label, choiceType);
        if (stringElements != null) {
            for (int i = 0; i < stringElements.length; i++) {
                append(stringElements[i], (imageElements != null && i < imageElements.length) ? imageElements[i] : null);
            }
        }
    }

    @Override
    public int size() {
        return strings.size();
    }

    @Override
    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public String getString(int elementNum) {
        return strings.get(elementNum);
    }

    @Override
    public Image getImage(int elementNum) {
        return images.get(elementNum);
    }

    @Override
    public int append(String stringPart, Image imagePart) {
        strings.add(stringPart);
        images.add(imagePart);
        if (selectedIndex == -1 && !strings.isEmpty()) {
            selectedIndex = 0;
        }
        return strings.size() - 1;
    }

    @Override
    public void insert(int elementNum, String stringPart, Image imagePart) {
        strings.add(elementNum, stringPart);
        images.add(elementNum, imagePart);
    }

    @Override
    public void delete(int elementNum) {
        strings.remove(elementNum);
        images.remove(elementNum);
    }

    @Override
    public void deleteAll() {
        strings.clear();
        images.clear();
        selectedIndex = -1;
    }

    @Override
    public void set(int elementNum, String stringPart, Image imagePart) {
        strings.set(elementNum, stringPart);
        images.set(elementNum, imagePart);
    }

    @Override
    public boolean isSelected(int elementNum) {
        return selectedIndex == elementNum;
    }

    @Override
    public void setSelectedIndex(int elementNum, boolean selected) {
        if (selected) {
            selectedIndex = elementNum;
        } else if (selectedIndex == elementNum) {
            selectedIndex = -1;
        }
    }

    @Override
    public int getSelectedFlags(boolean[] selectedArray) {
        int count = 0;
        for (int i = 0; i < strings.size() && i < selectedArray.length; i++) {
            selectedArray[i] = (selectedIndex == i);
            if (selectedArray[i]) count++;
        }
        return count;
    }

    @Override
    public void setSelectedFlags(boolean[] selectedArray) {
        for (int i = 0; i < selectedArray.length && i < strings.size(); i++) {
            if (selectedArray[i]) {
                selectedIndex = i;
                if (choiceType != MULTIPLE) break;
            }
        }
    }

    @Override
    public JComponent getSwingComponent() {
        return new JPanel(); // Placeholder
    }
}
