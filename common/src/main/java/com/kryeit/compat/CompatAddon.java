package com.kryeit.compat;

import com.kryeit.utils.Utils;

public enum CompatAddon {
    CREATE_DECO("createdeco"),
    LUCKPERMS("luckperms");

    private final String id;

    CompatAddon(String id) {
        this.id = id;
    }

    public boolean isLoaded() {
        return Utils.isModLoaded(id());
    }

    public String id() {
        return id;
    }

}
