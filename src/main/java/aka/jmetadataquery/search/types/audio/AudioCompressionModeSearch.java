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
import aka.jmetadataquery.search.constants.audio.CompressionModeEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;

/**
 * Audio compression mode search.
 *
 * @author charlottew
 */
public class AudioCompressionModeSearch extends Criteria<CompressionModeEnum, String> {

    private final Operator operation;
    private @NonNull final CompressionModeEnum compressionModeEnum;

    /**
     * Constructor.
     *
     * @param operation
     * @param compressionModeEnum Compression mode
     */
    public AudioCompressionModeSearch(final Operator operation, @NonNull final CompressionModeEnum compressionModeEnum) {
        super(compressionModeEnum);
        this.operation = operation;
        this.compressionModeEnum = compressionModeEnum;
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

        int i = -1;
        @NonNull
        final List<@NonNull JMetaDataAudio> audioStreams = jMetaData.getAudioStreams();
        for (final JMetaDataAudio jMetaDataAudio : audioStreams) {
            Integer idAsInteger = jMetaDataAudio.getIDAsInteger();
            if (idAsInteger == null) {
                idAsInteger = Integer.valueOf(i);
                i--;
            }
            final String mode = jMetaDataAudio.getCompressionModeAsString();
            if (mode != null) {
                final boolean match = conditionMatch(mode, this.compressionModeEnum.getValue(), this.operation);
                if (!result.containsKey(idAsInteger)) {
                    result.put(idAsInteger, Boolean.valueOf(match));
                }
            }
        }
        return result;
    }

}
