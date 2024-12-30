package org.comp.progiple.saterino.inventories.staticButtons;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.saterino.inventories.Button;
import org.comp.progiple.saterino.inventories.staticButtons.buttons.CloseButton;
import org.comp.progiple.saterino.inventories.staticButtons.buttons.UpdateItemsButton;
import org.comp.progiple.saterino.inventories.staticButtons.buttons.UpgradeLevelButton;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ButtonSetter {
    private final List<Button> buttonList = new ArrayList<>();
    public ButtonSetter(ConfigurationSection section, byte currentLevel) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            Button button = null;
            switch (key) {
                case "CLOSE" -> button = new CloseButton(itemSection);
                case "UPDATE_ITEMS" -> button = new UpdateItemsButton(itemSection);
                case "UPGRADE_LEVEL" -> {
                    assert itemSection != null;
                    button = new UpgradeLevelButton(itemSection, currentLevel);
                }
            }
            if (button != null) this.buttonList.add(button);
        }
    }
}
