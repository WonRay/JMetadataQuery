package aka.jmetadataquery.search.types.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.jmetadata.main.JMetaData;
import aka.jmetadata.main.JMetaDataAudio;
import aka.jmetadata.main.constants.format.FormatEnum;
import aka.jmetadataquery.helpers.SearchHelper;
import aka.jmetadataquery.search.Criteria;
import aka.jmetadataquery.search.constants.conditions.Operator;

/**
 * Audio Format search.
 *
 * @author charlottew
 */
public class AudioFormatSearch extends Criteria<FormatEnum, String> {

    private final Operator operation;
    private @NonNull final FormatEnum formatEnum;

    /**
     * Constructor.
     *
     * @param operation
     * @param formatEnum
     */
    public AudioFormatSearch(final Operator operation, @NonNull final FormatEnum formatEnum) {
        super(formatEnum);
        this.operation = operation;
        this.formatEnum = formatEnum;
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
        final List<@NonNull JMetaDataAudio> audioStreams = jMetaData.getAudioStreams();
        for (final JMetaDataAudio jMetaDataAudio : audioStreams) {
            Integer idAsInteger = jMetaDataAudio.getIDAsInteger();
            if (idAsInteger == null) {
                idAsInteger = Integer.valueOf(i);
                i--;
            }
            @Nullable
            final String formatCommercial = jMetaDataAudio.getFormatCommercialAsString();
            if (formatCommercial == null) {
                final String format = jMetaDataAudio.getFormatAsString();
                if (format != null) {
                    final String codec = this.formatEnum.getName();
                    final boolean match = conditionMatch(codec, format, this.operation);
                    if (!result.containsKey(idAsInteger)) {
                        result.put(idAsInteger, Boolean.valueOf(match));
                    }
                }
            } else {
                final String codec = this.formatEnum.getName();
                boolean match = conditionMatch(codec, formatCommercial, this.operation);
                if (!result.containsKey(idAsInteger)) {
                    result.put(idAsInteger, Boolean.valueOf(match));
                }
                if (!match) {
                    final String format = jMetaDataAudio.getFormatAsString();
                    if (format != null) {
                        match = conditionMatch(codec, format, this.operation);
                        if (!result.containsKey(idAsInteger)) {
                            result.put(idAsInteger, Boolean.valueOf(match));
                        }
                    }
                }
            }
        }
        return result;
    }

}
