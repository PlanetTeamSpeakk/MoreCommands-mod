package com.ptsmods.morecommands.miscellaneous;

public class MinecraftVersionData {
    public String id;
    public String name;
    public String release_target;
    public int world_version;
    public int protocol_version;
    public String build_time;
    public boolean stable;
    private Integer ver;

    public int getIVer() {
        return ver == null ? ver = Integer.parseInt(release_target.split("\\.")[1]) : ver;
    }
}
