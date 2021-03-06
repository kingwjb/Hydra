package com.findwise.hydra.stage.tika;

import java.io.IOException;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;

import com.findwise.hydra.DocumentFile;
import com.findwise.hydra.local.Local;
import com.findwise.hydra.local.LocalDocument;
import com.findwise.hydra.stage.AbstractProcessStage;
import com.findwise.hydra.stage.Parameter;
import com.findwise.hydra.stage.Stage;
import com.findwise.hydra.stage.tika.utils.TikaUtils;

/**
 * @author jwestberg
 */
@Stage(description="Stage that fetches any files attached to the document being processed and parses them with Tika. Any fields found by Tika will be stored in <filename>_*")
public class TikaStage extends AbstractProcessStage {
    @Parameter(name = "addMetaData", description = "Add the metadata to the document or not. Defaults to true")
    private boolean addMetaData = true;

	@Parameter(description = "Set to true, will also do language detection and add the field 'prefix_language' according to the prefix rules. Defaults to true")
	private boolean addLanguage = true;
    
	static private Parser parser = new AutoDetectParser();

	@Override
	public void process(LocalDocument doc) throws TikaException, SAXException, IOException {
		List<String> files = doc.getFileNames();
		for(String fileName : files) {
			DocumentFile<Local> df = doc.getFile(fileName);
			TikaUtils.enrichDocumentWithFileContents(doc, fileName.replace('.', '_')+"_", df.getStream(), parser, addMetaData, addLanguage);
		}
	}

	public boolean isAddMetaData() {
		return addMetaData;
	}

	public void setAddMetaData(boolean addMetaData) {
		this.addMetaData = addMetaData;
	}

	public boolean isAddLanguage() {
		return addLanguage;
	}

	public void setAddLanguage(boolean addLanguage) {
		this.addLanguage = addLanguage;
	}

}
