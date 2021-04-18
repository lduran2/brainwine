package brainwine.gameserver.server.commands;

import brainwine.gameserver.GameServer;
import brainwine.gameserver.entity.player.Player;
import brainwine.gameserver.server.PlayerCommand;
import brainwine.gameserver.server.RegisterCommand;
import brainwine.gameserver.zone.Zone;

@RegisterCommand(id = 24)
public class ZoneChangeCommand extends PlayerCommand {
    
    public String zoneName;
    
    @Override
    public void process(Player player) {
        Zone zone = GameServer.getInstance().getZoneManager().getZoneByName(zoneName);
        
        if(zone == null) {
            player.alert("Sorry, could not find a zone with name " + zoneName);
            return;
        } else if(zone == player.getZone()) {
            player.alert("You're already in " + zoneName);
            return;
        }
        
        player.changeZone(zone);
    }
}