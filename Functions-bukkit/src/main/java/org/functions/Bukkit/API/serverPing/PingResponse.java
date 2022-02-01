package org.functions.Bukkit.API.serverPing;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PingResponse
{
    private boolean isOnline;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;
    private int protocol;
    private String name;
    private String toString;

    public PingResponse(boolean isOnline, String motd, int onlinePlayers, int maxPlayers,int protocol,String name) {
        this.isOnline = isOnline;
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.protocol = protocol;
        this.name = name;
    }
    public PingResponse(String jsonString,ServerAddress address) {
        System.out.println(jsonString);

        if (jsonString == null || jsonString.isEmpty()) {
            toString = "Offline the server";
            motd = "Invalid ping response";return;
        }

        Object jsonObject = JSONValue.parse(jsonString);

        if (!(jsonObject instanceof JSONObject)) {
            toString = "Offline the server";
            motd = "Invalid ping response";return;
        }

        JSONObject json = (JSONObject) jsonObject;
        isOnline = true;
        toString = jsonString;

        Object descriptionObject = json.get("description");
        if (descriptionObject != null) {
            if (descriptionObject instanceof JSONObject) {
                String descriptionString = ((JSONObject) descriptionObject).toJSONString();
                try {
                    motd = descriptionString;
                } catch (Exception e) {
                    motd = "Invalid ping response";
                }
            } else {
                motd = descriptionObject.toString();
            }
        } else {
            motd = "Invalid ping response (description not found)";
        }

        Object playersObject = json.get("players");

        if (playersObject instanceof JSONObject) {
            JSONObject playersJson = (JSONObject) playersObject;

            Object onlineObject = playersJson.get("online");
            if (onlineObject instanceof Number) {
                onlinePlayers = ((Number) onlineObject).intValue();
            }

            Object maxObject = playersJson.get("max");
            if (maxObject instanceof Number) {
                maxPlayers = ((Number) maxObject).intValue();
            }
        }
        Object versionObject = json.get("version");
        if (versionObject instanceof JSONObject) {
            JSONObject versionJson = (JSONObject) versionObject;

            Object protocolObject = versionJson.get("protocol");
            if (protocolObject instanceof Number) {
                protocol = ((Number) protocolObject).intValue();
            }
            Object nameObject = ((JSONObject) versionObject).get("name");
            if (nameObject instanceof JSONObject) {
                String nameString = ((JSONObject) nameObject).toJSONString();
                try {
                    name = nameString;
                } catch (Exception e) {
                    name = "Invalid ping response";
                }
            } else {
                name = nameObject.toString();
            }
        }
    }
    public String toString() {
        return toString;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getMotd() {
        return motd.replace("{\"text\":\"","").replace("\"}","");
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
