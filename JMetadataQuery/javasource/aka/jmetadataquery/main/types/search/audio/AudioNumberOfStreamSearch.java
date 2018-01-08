package aka.jmetadataquery.main.types.search.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.BinaryCondition.Op;

import aka.jmetadata.main.JMetaData;
import aka.jmetadata.main.JMetaDataAudio;
import aka.jmetadataquery.main.types.search.Criteria;
import aka.jmetadataquery.main.types.search.helpers.SearchHelper;

/**
 * Audio number of stream search.
 *
 * @author charlottew
 */
public class AudioNumberOfStreamSearch extends Criteria<Long, Long> {

    private final Op operation;
    private @NonNull final Long numberOfStream;

    /**
     * Constructor.
     *
     * @param operation
     * @param numberOfStream
     */
    public AudioNumberOfStreamSearch(final BinaryCondition.Op operation, @NonNull final Long numberOfStream) {
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

        result.put(Integer.valueOf(Integer.MIN_VALUE), Boolean.valueOf(match));
        return result;
    }

}
