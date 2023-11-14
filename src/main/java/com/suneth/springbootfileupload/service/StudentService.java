package com.suneth.springbootfileupload.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.suneth.springbootfileupload.model.Student;
import com.suneth.springbootfileupload.model.StudentCsvRepresentation;
import com.suneth.springbootfileupload.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;

    /**
     * Uploads student data from a CSV file to the database.
     *
     * @param file The CSV file containing student data.
     * @return The number of students uploaded.
     * @throws IOException If an error occurs during file processing.
     */

    public Integer uploadStudent(MultipartFile file) throws IOException {
        // Parse the CSV file and save the students to the database
        Set<Student> students = parseCsv(file);
        studentRepo.saveAll(students);
        return students.size();
    }
    /**
     * Parses a CSV file into a set of Student objects.
     *
     * @param file The CSV file to be parsed.
     * @return A set of Student objects representing the data in the CSV file.
     * @throws IOException If an error occurs during file processing.
     */

    private Set<Student> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Configuration for mapping CSV columns to StudentCsvRepresentation fields
            HeaderColumnNameMappingStrategy<StudentCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StudentCsvRepresentation.class);

            // Create a CSV to Bean parser with the specified configuration
            CsvToBean<StudentCsvRepresentation> csvToBean =
                    new CsvToBeanBuilder<StudentCsvRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();

            // Parse the CSV data and convert it into Student objects
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> Student.builder()
                            .firstName(csvLine.getFname())
                            .lastName(csvLine.getLname())
                            .age(csvLine.getAge())
                            .build()
                    )
                    .collect(Collectors.toSet());
        }
    }
}
