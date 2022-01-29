package org.functions.Bukkit.Main.functions.Utitils;

import org.bukkit.Bukkit;

public enum ProtocolVersion {
    UNKNOWN("Unknown"),
    PROXY("Proxy"),
    V1_18_1(757),
    V1_18(757),
    V1_17_1(756),
    V1_17(755),
    V1_16_5(754),
    V1_16_4(754),
    V1_16_3(753),
    V1_16_2(751),
    V1_16_1(736),
    V1_16(735),
    V1_15_2(578),
    V1_15_1(575),
    V1_15(573),
    V1_14_4(498),
    V1_14_3(490),
    V1_14_2(485),
    V1_14_1(480),
    V1_14(477),
    V1_13_2(404),
    V1_13_1(401),
    V1_13(393),
    V1_12_2(340),
    V1_12_1(338),
    V1_12(335),
    V1_11_2(316),
    V1_11_1(316),
    V1_11(315),
    V1_10_2(210),
    V1_10_1(210),
    V1_10(210),
    V1_9_4(110),
    V1_9_3(110),
    V1_9_2(109),
    V1_9_1(108),
    V1_9(107),
    V1_8(47),
    V1_7_10(5),
    V1_7_9(5),
    V1_7_8(5),
    V1_7_7(5),
    V1_7_6(5),
    V1_7_5(4),
    V1_7_4(4),
    V1_7_2(4),
    V1_6_4(78),
    V1_6_2(74),
    V1_6_1(73),
    V1_5_2(61),
    V1_5_1(60),
    V1_5(60),
    V1_4_7(51),
    V1_4_6(51);

    private final int networkId;
    private final int minorVersion;
    private final String friendlyName;

    private ProtocolVersion(int networkId) {
        this.networkId = networkId;
        this.minorVersion = Integer.parseInt(this.toString().split("_")[1]);
        this.friendlyName = this.toString().substring(1).replace("_", ".");
    }

    private ProtocolVersion(String friendlyName) {
        this.networkId = 999;
        this.minorVersion = 18;
        this.friendlyName = friendlyName;
    }

    public int getNetworkId() {
        return this.networkId;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public static ProtocolVersion fromFriendlyName(String friendlyName) {
        if (friendlyName.startsWith("1.8")) {
            return V1_8;
        } else {
            try {
                return valueOf("V" + friendlyName.replace(".", "_"));
            } catch (IllegalArgumentException var2) {
                return UNKNOWN;
            }
        }
    }

    public static ProtocolVersion fromNetworkId(int networkId) {
        ProtocolVersion[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ProtocolVersion v = var1[var3];
            if (networkId == v.getNetworkId()) {
                return v;
            }
        }

        return UNKNOWN;
    }
    public static ProtocolVersion getServerVersion() {
        return ProtocolVersion.fromFriendlyName(Bukkit.getBukkitVersion().split("-")[0]);
    }
}
