package com.kryeit.registry;

import com.kryeit.content.jar_of_tips.JarOfTipsItem;
import com.tterrag.registrate.util.entry.ItemEntry;

import static com.kryeit.Missions.REGISTRATE;

public class ModItems {

    public static final ItemEntry<JarOfTipsItem> JAR_OF_TIPS =
            REGISTRATE.item("jar_of_tips", JarOfTipsItem::empty)
                    .register();

    public void register() { }
}
