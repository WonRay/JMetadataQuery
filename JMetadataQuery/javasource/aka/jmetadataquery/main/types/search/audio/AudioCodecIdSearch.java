package aka.jmetadataquery.main.types.search.audio;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.BinaryCondition.Op;

import aka.jmetadata.main.JMetaData;
import aka.jmetadata.main.JMetaDataAudio;
import aka.jmetadata.main.constants.codecs.interfaces.CodecEnum;
import aka.jmetadataquery.main.types.search.Criteria;

/**
 * Audio Codec ID search.
 *
 * @author charlottew
 */
public class AudioCodecIdSearch extends Criteria<CodecEnum, String> {

    private final Op operation;
    private @NonNull final CodecEnum codecEnum;

    /**
     * Constructor.
     *
     * @param operation
     * @param codecEnum
     */
    public AudioCodecIdSearch(final BinaryCondition.Op operation, @NonNull final CodecEnum codecEnum) {
        super(codecEnum);
        this.operation = operation;
        this.codecEnum = codecEnum;
    }

    @Override
    public boolean matchCriteria(@NonNull final JMetaData jMetaData) {
        boolean result = false;

        @NonNull
        final List<@NonNull JMetaDataAudio> audioStreams = jMetaData.getAudioStreams();
        for (final JMetaDataAudio jMetaDataAudio : audioStreams) {
            final String codecId = jMetaDataAudio.getCodecIDAsString();
            final boolean allMustMatch = this.operation == BinaryCondition.Op.NOT_EQUAL_TO;
            if (codecId != null) {
                for (final String codec : this.codecEnum.getValues()) {
                    result = conditionMatch(codec, codecId, this.operation);
                    if (result && !allMustMatch) {
                        // just break
                        break;
                    }
                }
            }
            if (result) {
                break;
            }
        }
        return result;
    }

}