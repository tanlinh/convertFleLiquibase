package com.example.demo;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.yaml.snakeyaml.Yaml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.ChangeLogSerializerFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
public class DemoApplication {

	public static void main(String[] args) throws Exception {
		System.out.println("adasdasd");
		String input = "F:\\demo\\src\\main\\resources\\test.yaml";
		String output = "F:\\demo\\src\\main\\resources\\dbchangelog.xml";

		ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
		ChangeLogParser parser = null;
		try {
			parser = ChangeLogParserFactory.getInstance().getParser(FilenameUtils.getName(input), resourceAccessor);
			DatabaseChangeLog changeLog = parser
					.parse(input, new ChangeLogParameters(), resourceAccessor);

			ChangeLogSerializer serializer = ChangeLogSerializerFactory.getInstance().getSerializer(FilenameUtils.getExtension(output));
			try (OutputStream ymlOutputstream = Files.newOutputStream(Paths.get(output))) {
				serializer.write(changeLog.getChangeSets(), ymlOutputstream);
			} catch (IOException e) {
				throw new RuntimeException("Unable to write output file " + output, e);
			}
		} catch (LiquibaseException e) {
			throw new RuntimeException("Unable to process liquibase file " + input, e);
		}
//		return null;
	}

	private static String getStringFromDocument(Document doc) throws Exception {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		return writer.toString();
	}

}
