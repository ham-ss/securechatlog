package marvtechnology.securechatlog.chat.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class MuteEntry {

    private final UUID uuid;
    private final String playerName;
    private final Instant mutedUntil;
    private final String reason;

    public MuteEntry(UUID uuid, String playerName, Instant mutedUntil, String reason) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.mutedUntil = mutedUntil;
        this.reason = reason;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Instant getMutedUntil() {
        return mutedUntil;
    }

    public String getReason() {
        return reason;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(mutedUntil);
    }

    public String getFormattedUntil() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault())
                .format(mutedUntil);
    }

    public String getRemainingFormatted() {
        long totalSec = mutedUntil.getEpochSecond() - Instant.now().getEpochSecond();
        if (totalSec <= 0) return "0分";
        long hours = totalSec / 3600;
        long minutes = (totalSec % 3600) / 60;
        return (hours > 0 ? hours + "時間" : "") + minutes + "分";
    }
}
