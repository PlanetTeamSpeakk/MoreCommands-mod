package com.ptsmods.morecommands.clientoption;

import com.ptsmods.morecommands.miscellaneous.Command;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class BooleanClientOption extends ClientOption<Boolean> {
    BooleanClientOption(Boolean defaultValue) {
        super(defaultValue);
    }

    BooleanClientOption(Boolean defaultValue, BiConsumer<Boolean, Boolean> updateConsumer) {
        super(defaultValue, updateConsumer);
    }

    BooleanClientOption(Boolean defaultValue, String... comments) {
        super(defaultValue, comments);
    }

    BooleanClientOption(Boolean defaultValue, BiConsumer<Boolean, Boolean> updateConsumer, String... comments) {
        super(defaultValue, updateConsumer, comments);
    }

    @Override
    public String getValueString() {
        return String.valueOf(getValueRaw());
    }

    @Override
    public void setValueString(String s) {
        setValue("true".equals(s));
    }

    @Override
    public Object createButton(int x, int y, String name, Runnable init) {
        return new ButtonWidget(x, y, 150, 20, createButtonText(name), btn -> {
            setValue(!getValueRaw());
            btn.setMessage(createButtonText(name));
            init.run();
            ClientOptions.write();
        });
    }

    @Override
    public Text createButtonText(String name) {
        return new LiteralText(name + " : " + Command.formatFromBool(getValueRaw()) + String.valueOf(getValueRaw()).toUpperCase());
    }
}
