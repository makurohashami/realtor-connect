package com.kotyk.realtorconnect.file;

import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.service.file.FileParamsGenerator;
import com.kotyk.realtorconnect.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class FileServiceITest {

    @Autowired
    FileService fileService;
    @Autowired
    FileParamsGenerator fileParamsGenerator;

    @Test
    public void generateParamsForAvatarTest() {
        //when
        Map<String, Object> params = fileParamsGenerator.generateParamsForAvatar(new User());

        //then
        assertThat(params, notNullValue());
    }

    @Test
    public void generateParamsForRealEstatePhoto() {
        //when
        Map<String, Object> params = fileParamsGenerator.generateParamsForRealEstatePhoto(new RealEstate());

        //then
        assertThat(params, notNullValue());
    }

    @Test
    public void uploadFileTest() {
        //when
        FileUploadResponse response = fileService.uploadFile(null, new HashMap<>());

        //then
        assertThat(response, notNullValue());
    }

    @Test
    public void deleteFileWithoutExceptionTest() {
        //when
        fileService.deleteFile("filename.jpg");

    }

    @Test
    public void deleteFolderWithoutExceptionTest() {
        //when
        fileService.deleteFolder("/folder");
    }

}
