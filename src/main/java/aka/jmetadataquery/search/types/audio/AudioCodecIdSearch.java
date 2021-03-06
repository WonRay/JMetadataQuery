package aka.jmetadataquery.search.types.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.JMetaData;
import aka.jmetadata.main.JMetaDataAudio;
import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import aka.jmetadataquery.helpers.SearchHelper;
import aka.jmetadataquery.search.Criteria;
import aka.jmetadataquery.search.constants.conditions.Operator;

/**
 * Audio Codec ID search.
 *
 * @author charlottew
 */
public class AudioCodecIdSearch extends Criteria<CodecEnum, String> {

    private final Operator operation;
    private @NonNull final CodecEnum codecEnum;

    /**
     * Constructor.
     *
     * @param operation
     * @param codecEnum
     */
    public AudioCodecIdSearch(final Operator operation, @NonNull final CodecEnum codecEnum) {
        super(codecEnum);
        this.operation = operation;
        this.codecEnum = codecEnum;
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
            final String codecId = jMetaDataAudio.getCodecIDAsString();
            Integer idAsInteger = jMetaDataAudio.getIDAsInteger();
            if (idAsInteger == null) {
                idAsInteger = Integer.valueOf(i);
                i--;
            }
            if (codecId != null) {
                for (final String codec : this.codecEnum.getValues()) {
                    final boolean match = conditionMatch(codec, codecId, this.operation);
                    if (!result.containsKey(idAsInteger)) {
                        result.put(idAsInteger, Boolean.valueOf(match));
                    }
                }
            }
        }
        return result;
    }

}
