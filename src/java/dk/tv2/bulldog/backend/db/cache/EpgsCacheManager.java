package dk.tv2.bulldog.backend.db.cache;

import dk.tv2.bulldog.backend.db.entities.Epgs;
import dk.tv2.bulldog.backend.db.managers.*;
import java.util.Map;

/**
 *
 * @author hjep
 */
public class EpgsCacheManager extends EpgsManager {

    @Override
    public Map<String, Epgs> loadProgramsWithInTime(int channelId, long startTime, long endTime) {
        return super.loadProgramsWithInTime(channelId, startTime, endTime);
    }

    @Override
    public Map<String, Epgs> loadProgramsWithInTime(int channelId, long startTime) {
        return super.loadProgramsWithInTime(channelId, startTime);
    }


    @Override
    public Map<String, Epgs> loadPrgramsByCategoryForChannelWithInTime(String category, int channelId, long startTime) {
        return super.loadPrgramsByCategoryForChannelWithInTime(category, channelId, startTime);
    }

    @Override
    public Epgs load(int channelId, int programId) {
        return super.load(channelId, programId);
    }

}
