package collector.engine;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public class MyLanguageDetector {

	public static LanguageDetector languageDetector;
	public static TextObjectFactory textObjectFactory;
	public static void load() throws IOException{
		//load all languages:
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        //build language detector:
        languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .build();

        //create a text object factory
        textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

	}
	public static String detect(String text) {
		
        //query:
        TextObject textObject = textObjectFactory.forText(text.toLowerCase());
        
        Optional<LdLocale> lang = languageDetector.detect(textObject);
        
        String stringLang = "";
        try{
        	stringLang = lang.get().getLanguage();
        }catch(Exception e){
        	return "N/A";
        }
        return stringLang;
	}
}
