package com.amazon.speech.json;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

/**
 * Simple command line utility used to generate a JSON schema and save it to a text file.
 */
public class SchemaGenerator {
    private SchemaGenerator() {
    }

    /**
     * Simple command line utility to generate and save JSON schema to a text file.
     *
     * @param args
     *            [0] is the canonical class name. [1] is the full file name to write the schema to.
     */
    public static void main(String[] args) {
        String canonicalClassName = args[0];
        String outputFile = args[1];

        if (!validateIntput(canonicalClassName, outputFile)) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonSchema schema = fetchJsonSchema(canonicalClassName, mapper);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), schema);
        } catch (IOException ex) {
            System.out.println(String.format(
                    "An error occurred while saving the schema for [%s] to [%s]",
                    canonicalClassName, outputFile));
            ex.printStackTrace();
        }
    }

    private static boolean validateIntput(String className, String outputFile) {
        if (StringUtils.isEmpty(className)) {
            System.out.println("Canonical class name must be defined.");
            return false;
        }

        if (StringUtils.isEmpty(outputFile)) {
            System.out.println("Output file name must be defined.");
            return false;
        }

        return true;
    }

    private static JsonSchema fetchJsonSchema(String canonicalClassName, ObjectMapper mapper) {
        SchemaFactoryWrapper wrapper = new SchemaFactoryWrapper();
        try {
            Class<?> jsonClass = Class.forName(canonicalClassName);
            mapper.acceptJsonFormatVisitor(jsonClass, wrapper);
            return wrapper.finalSchema();
        } catch (JsonMappingException ex) {
            System.out.println("Unable to generate the JSON schema");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("The provided canonical class name is invalid: "
                    + canonicalClassName);
        }

        return null;
    }
}
