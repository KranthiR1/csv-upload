package com.test.csvupload.controller;

import com.test.csvupload.CsvUploadApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CsvUploadApp.class, webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class CsvControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testPostUploadFile_whenValidRequest_thenReturnValidResponse() throws Exception {

        mvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload").file(getMockMultiPartFileCsv("valid.csv"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Uploaded the file successfully: valid.csv"));
    }

    @Test
    public void testPostUploadFile_whenInValidRequest_thenReturnErrorResponse() throws Exception {

        mvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload")
                        .file(getMockMultiPartFileCsv("invalid.csv")))
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not upload the file: invalid.csv!"));
    }

    @Test
    public void testPostUploadFile_whenInValidFileType_thenReturnBadRequestResponse() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private MockMultipartFile getMockMultiPartFileCsv(String fileName) throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                fileName,
                "text/csv",
                this.getClass().getClassLoader()
                        .getResourceAsStream(fileName)
        );
        return file;
    }
}
