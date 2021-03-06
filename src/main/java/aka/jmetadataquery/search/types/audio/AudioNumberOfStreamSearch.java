package aka.jmetadataquery.search.types.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.JMetaData;
import aka.jmetadata.main.JMetaDataAudio;
import aka.jmetadataquery.helpers.SearchHelper;
import aka.jmetadataquery.search.Criteria;
import aka.jmetadataquery.search.constants.conditions.Operator;

/**
 * Audio number of stream search.
 *
 * @author charlottew
 */
public class AudioNumberOfStreamSearch extends Criteria<Long, Long> {

    private final Operator operation;
    private @NonNull final Long numberOfStream;

    /**
     * Constructor.
     *
     * @param operation
     * @param numberOfStream
     */
    public AudioNumberOfStreamSearch(final Operator operation, @NonNull final Long numberOfStream) {
        super(numberOfStream);
        this.operation = operation;
        this.numberOfStream = numberOfStream;
    }

    @Override
    public boolean matchCriteria(@NonNull final JMetaData jMetaData) {
        final Map<@NonNull Integer, Boolean> map = getStreamsIDInFileMatchingCriteria(jMetaData);
        final List<@NonNull Map<@NonNull Integer, Boolean>> idMapList = new ArrayList<>();
        idMapList.add(map);
        return SearchHelper.isMatching(idMapList, 1, false);
    }

    @Override
    public @NonNull Map<@NonNull Integer, Boolean> getStreamsIDInFileMatchingCriteria(@NonNull final JMetaData jMetaData) {
        final Map<@NonNull Integer, Boolean> result = new HashMap<>();

        final List<@NonNull JMetaDataAudio> audioStreams = jMetaData.getAudioStreams();
        final Long size = Long.valueOf(audioStreams.size());
        final boolean match = conditionMatch(size, this.numberOfStream, this.operation);

        int i = -1;
        for (final JMetaDataAudio jMetaDataAudio : audioStreams) {
            Integer idAsInteger = jMetaDataAudio.getIDAsInteger();
            if (idAsInteger == null) {
                idAsInteger = Integer.valueOf(i);
                i--;
            }
            result.put(idAsInteger, Boolean.valueOf(match));
        }

        return result;
    }

}
